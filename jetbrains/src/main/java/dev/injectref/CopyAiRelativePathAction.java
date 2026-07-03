package dev.injectref;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformCoreDataKeys;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileSystemItem;

import java.awt.datatransfer.StringSelection;

public class CopyAiRelativePathAction extends AnAction {
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
    public void update(AnActionEvent event) {
        Project project = event.getProject();
        VirtualFile file = resolveFile(event);
        event.getPresentation().setEnabledAndVisible(project != null && !shouldRejectTarget(file != null, file != null && file.isDirectory()));
    }

    static VirtualFile resolveFile(AnActionEvent event) {
        VirtualFile[] selectedFiles = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
        VirtualFile selectedFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
        VirtualFile[] projectViewFiles = toVirtualFiles(event.getData(PlatformCoreDataKeys.PSI_ELEMENT_ARRAY));
        VirtualFile projectViewFile = toVirtualFile(event.getData(CommonDataKeys.PSI_ELEMENT));
        VirtualFile editorFile = null;

        var editor = event.getData(CommonDataKeys.EDITOR);

        if (editor != null) {
            editorFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
        }

        return resolveValue(selectedFiles, selectedFile, projectViewFiles, projectViewFile, editorFile);
    }

    static <T> T resolveValue(T[] selectedValues, T selectedValue, T fallbackValue) {
        return resolveValue(selectedValues, selectedValue, null, null, fallbackValue);
    }

    static <T> T resolveValue(T[] selectedValues, T selectedValue, T[] projectViewValues, T projectViewValue, T fallbackValue) {
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

        if (projectViewValues != null) {
            if (projectViewValues.length > 1) {
                return null;
            }

            if (projectViewValues.length == 1) {
                return projectViewValues[0];
            }
        }

        if (projectViewValue != null) {
            return projectViewValue;
        }

        return fallbackValue;
    }

    private static VirtualFile[] toVirtualFiles(PsiElement[] elements) {
        if (elements == null) {
            return null;
        }

        VirtualFile[] files = new VirtualFile[elements.length];

        for (int i = 0; i < elements.length; i++) {
            VirtualFile file = toVirtualFile(elements[i]);

            if (file == null) {
                return null;
            }

            files[i] = file;
        }

        return files;
    }

    private static VirtualFile toVirtualFile(PsiElement element) {
        if (element instanceof PsiFileSystemItem fileSystemItem) {
            return fileSystemItem.getVirtualFile();
        }

        return null;
    }

    static boolean shouldRejectTarget(boolean hasTarget, boolean isDirectory) {
        return !hasTarget;
    }
}
