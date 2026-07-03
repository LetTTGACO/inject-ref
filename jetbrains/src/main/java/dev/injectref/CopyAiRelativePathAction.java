package dev.injectref;

import com.intellij.ide.actions.CopyReferenceUtil;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileSystemItem;

import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

public class CopyAiRelativePathAction extends DumbAwareAction {
    private static final String ERROR_MESSAGE = "无法复制 AI 相对路径：请先选择项目内文件或目录。";

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        VirtualFile file = resolveFile(event);

        String reference = project == null || shouldRejectTarget(file != null, file != null && file.isDirectory())
            ? null
            : ReferenceFormatter.formatAbsolutePath(project.getBasePath(), file.getPath());

        if (reference == null) {
            Messages.showErrorDialog(project, ERROR_MESSAGE, "Inject Ref");
            return;
        }

        CopyPasteManager.getInstance().setContents(new StringSelection(reference));
    }

    @Override
    public ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void update(AnActionEvent event) {
        Project project = event.getProject();
        VirtualFile file = resolveFile(event);
        event.getPresentation().setEnabledAndVisible(project != null && !shouldRejectTarget(file != null, file != null && file.isDirectory()));
    }

    static VirtualFile resolveFile(AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        List<VirtualFile> copyReferenceFiles = toVirtualFiles(getElementsToCopy(editor, event));
        VirtualFile[] selectedFiles = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
        VirtualFile selectedFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
        VirtualFile editorFile = null;

        if (editor != null) {
            editorFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
        }

        return resolveValue(copyReferenceFiles, selectedFiles, selectedFile, editorFile);
    }

    static <T> T resolveValue(List<T> copyReferenceValues, T[] selectedValues, T selectedValue, T fallbackValue) {
        if (copyReferenceValues != null) {
            if (copyReferenceValues.size() > 1) {
                return null;
            }

            if (copyReferenceValues.size() == 1) {
                return copyReferenceValues.get(0);
            }
        }

        if (selectedValues != null) {
            if (selectedValues.length > 1) {
                return null;
            }

            if (selectedValues.length == 1) {
                return selectedValues[0];
            }
        }

        if (selectedValue != null) {
            return selectedValue;
        }

        return fallbackValue;
    }

    private static List<PsiElement> getElementsToCopy(Editor editor, AnActionEvent event) {
        try {
            return CopyReferenceUtil.getElementsToCopy(editor, event.getDataContext());
        } catch (IndexNotReadyException ignored) {
            return List.of();
        }
    }

    private static List<VirtualFile> toVirtualFiles(List<PsiElement> elements) {
        if (elements == null) {
            return null;
        }

        List<VirtualFile> files = new ArrayList<>();

        for (PsiElement element : elements) {
            VirtualFile file = toVirtualFile(element);

            if (file != null) {
                files.add(file);
            }
        }

        return files;
    }

    private static VirtualFile toVirtualFile(PsiElement element) {
        if (element instanceof PsiFileSystemItem fileSystemItem) {
            return fileSystemItem.getVirtualFile();
        }

        var containingFile = element.getContainingFile();
        return containingFile == null ? null : containingFile.getVirtualFile();
    }

    static boolean shouldRejectTarget(boolean hasTarget, boolean isDirectory) {
        return !hasTarget;
    }
}
