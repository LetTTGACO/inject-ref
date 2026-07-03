package dev.injectref;

import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class CopyAiRelativePathActionTest {
    @Test
    public void rejectsMultipleSelectedFiles() {
        String first = new String("first");
        String second = new String("second");

        assertNull(CopyAiRelativePathAction.resolveValue(new String[] { first, second }, null, null));
    }

    @Test
    public void usesSingleSelectedFileFromArray() {
        String selected = new String("selected");
        String fallback = new String("fallback");

        assertSame(selected, CopyAiRelativePathAction.resolveValue(new String[] { selected }, "ignored", fallback));
    }

    @Test
    public void fallsBackToSelectedFileWhenArrayIsMissing() {
        String selected = new String("selected");

        assertSame(selected, CopyAiRelativePathAction.resolveValue(null, selected, "editor"));
    }

    @Test
    public void fallsBackToEditorFileWhenSelectionArrayIsEmpty() {
        String editor = new String("editor");

        assertSame(editor, CopyAiRelativePathAction.resolveValue(new String[0], null, editor));
    }
}
