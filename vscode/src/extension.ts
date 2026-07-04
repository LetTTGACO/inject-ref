import * as vscode from 'vscode';
import { toAiReference, toSingleAiReference } from './reference';
import { shouldRejectCopyRequest } from './selection';

const COMMAND_ID = 'inject-ref.copyAiRelativePath';
const NATIVE_COPY_RELATIVE_PATH_COMMAND_ID = 'copyRelativeFilePath';
const ERROR_MESSAGE = '无法复制 AI 相对路径：请先选择项目内文件或目录。';

export function activate(context: vscode.ExtensionContext): void {
  const disposable = vscode.commands.registerCommand(COMMAND_ID, async (uri?: vscode.Uri, selectedUris?: vscode.Uri[]) => {
    const selectedCount = selectedUris?.length ?? 0;
    const targetUri = uri ?? (selectedCount === 1 ? selectedUris?.[0] : undefined);

    if (shouldRejectCopyRequest({ selectedCount, isDirectory: false })) {
      vscode.window.showErrorMessage(ERROR_MESSAGE);
      return;
    }

    if (!targetUri) {
      await copyUsingNativeRelativePathCommand();
      return;
    }

    await copyUriReference(targetUri);
  });

  context.subscriptions.push(disposable);
}

async function copyUriReference(targetUri: vscode.Uri): Promise<void> {
  if (targetUri.scheme !== 'file' || !vscode.workspace.getWorkspaceFolder(targetUri)) {
    vscode.window.showErrorMessage(ERROR_MESSAGE);
    return;
  }

  const relativePath = vscode.workspace.asRelativePath(targetUri, false);
  const reference = toAiReference(relativePath);

  if (!reference) {
    vscode.window.showErrorMessage(ERROR_MESSAGE);
    return;
  }

  await vscode.env.clipboard.writeText(reference);
}

async function copyUsingNativeRelativePathCommand(): Promise<void> {
  const previousClipboard = await vscode.env.clipboard.readText();

  await vscode.commands.executeCommand(NATIVE_COPY_RELATIVE_PATH_COMMAND_ID);

  const copiedRelativePath = await vscode.env.clipboard.readText();
  const reference = toSingleAiReference(copiedRelativePath);

  if (!reference || (!vscode.window.activeTextEditor && copiedRelativePath === previousClipboard)) {
    await vscode.env.clipboard.writeText(previousClipboard);
    vscode.window.showErrorMessage(ERROR_MESSAGE);
    return;
  }

  await vscode.env.clipboard.writeText(reference);
}

export function deactivate(): void {}
