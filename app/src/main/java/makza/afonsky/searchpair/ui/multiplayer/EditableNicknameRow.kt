package makza.afonsky.searchpair.ui.multiplayer

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import makza.afonsky.searchpair.ui.theme.ColorRedDark

@Composable
fun EditableNicknameRow(
    nickname: String,
    draft: String,
    isEditing: Boolean,
    onStartEdit: () -> Unit,
    onDraftChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    editable: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(0.85f)
            .border(2.dp, ColorRedDark, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        if (isEditing) {
            BasicTextField(
                value = draft,
                onValueChange = onDraftChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onConfirm() }),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "✓",
                    fontSize = 22.sp,
                    color = ColorRedDark,
                    modifier = Modifier.clickable { onConfirm() },
                )
                Text(
                    text = "✕",
                    fontSize = 22.sp,
                    color = ColorRedDark,
                    modifier = Modifier.clickable { onCancel() },
                )
            }
        } else {
            Text(
                text = nickname,
                fontSize = 20.sp,
                color = ColorRedDark,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "✎",
                fontSize = 22.sp,
                color = if (editable) ColorRedDark else ColorRedDark.copy(alpha = 0.35f),
                modifier = if (editable) {
                    Modifier.clickable { onStartEdit() }
                } else {
                    Modifier
                },
            )
        }
    }
}
