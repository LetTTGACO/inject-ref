import * as vscode from 'vscode';
import { toAiReference } from './reference';
import { shouldRejectCopyRequest } from './selection';

const COMMAND_ID = 'inject-ref.copyAiRelativePath';
const ERROR_MESSAGE = '无法复制 AI 相对路径：请先选择项目内文件。';

export function activate(context: vscode.ExtensionContext): void {
  const disposable = vscode.commands.registerCommand(COMMAND_ID, async (uri?: vscode.Uri, selectedUris?: vscode.Uri[]) => {
    const targetUri = uri ?? vscode.window.activeTextEditor?.document.uri;
    const selectedCount = selectedUris?.length ?? 0;

    if (!targetUri || targetUri.scheme !== 'file' || !vscode.workspace.getWorkspaceFolder(targetUri)) {
      vscode.window.showErrorMessage(ERROR_MESSAGE);
      return;
    }

    if (selectedCount > 1) {
      vscode.window.showErrorMessage(ERROR_MESSAGE);
      return;
    }

    const isDirectory = (await vscode.workspace.fs.stat(targetUri)).type === vscode.FileType.Directory;

    if (shouldRejectCopyRequest({ selectedCount, isDirectory })) {
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
  });

  context.subscriptions.push(disposable);
}

export function deactivate(): void {}
