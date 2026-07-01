package makza.afonsky.searchpair.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import makza.afonsky.searchpair.R
import makza.afonsky.searchpair.data.GameRepository
import makza.afonsky.searchpair.ui.theme.ColorOrange2

@Composable
fun ConfirmDialog(
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .background(ColorOrange2.copy(alpha = 0.95f), RoundedCornerShape(16.dp))
                .padding(24.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = message,
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    GameButton(
                        text = stringResource(R.string.button_reset_progress_yes),
                        onClick = onConfirm,
                    )
                    GameButton(
                        text = stringResource(R.string.button_reset_progress_no),
                        onClick = onDismiss,
                    )
                }
            }
        }
    }
}

@Composable
fun ChestDialog(
    kitCounts: GameRepository.KitCounts,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .background(ColorOrange2.copy(alpha = 0.95f), RoundedCornerShape(16.dp))
                .padding(20.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = stringResource(R.string.dialog_chest_title),
                    style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                ) {
                    repeat(kitCounts.small) {
                        Image(
                            painter = painterResource(R.drawable.restorehealth),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                            contentScale = ContentScale.Fit,
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                ) {
                    repeat(kitCounts.medium) {
                        Image(
                            painter = painterResource(R.drawable.restorehealth2),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                            contentScale = ContentScale.Fit,
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                ) {
                    repeat(kitCounts.big) {
                        Image(
                            painter = painterResource(R.drawable.restorehealth3),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                            contentScale = ContentScale.Fit,
                        )
                    }
                }

                GameButton(
                    text = stringResource(R.string.button_close),
                    onClick = onDismiss,
                )
            }
        }
    }
}
