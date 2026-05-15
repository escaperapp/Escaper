package io.escaper.escaperapp.presentation.argsinput

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import escaper.composeapp.generated.resources.EscaperRes
import escaper.composeapp.generated.resources.bind_addr_arg_description
import escaper.composeapp.generated.resources.bind_iface4_arg_description
import escaper.composeapp.generated.resources.bind_iface6_arg_description
import escaper.composeapp.generated.resources.bind_linklocal_arg_description
import escaper.composeapp.generated.resources.daemon_flag_description
import escaper.composeapp.generated.resources.dry_run_flag_description
import escaper.composeapp.generated.resources.pidfile_arg_description
import escaper.composeapp.generated.resources.tpws_debug_mode_description
import escaper.composeapp.generated.resources.uid_arg_description
import escaper.composeapp.generated.resources.user_arg_description
import escaper.composeapp.generated.resources.version_flag_description
import io.escaper.escaperapp.domain.ExecutableType
import io.escaper.escaperapp.domain.args.ArgumentKey
import io.escaper.escaperapp.presentation.common.EscaperTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ArgumentKeySelector(
    executableType: ExecutableType,
    onSelectKey: (ArgumentKey) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = ArgumentKey.getEntriesForExecType(executableType),
            key = { it.cliKey }
        ) { key ->
            ArgumentKeyItem(
                key = key,
                onSelectKey = onSelectKey
            )
        }
    }
}

@Composable
private fun ArgumentKeyItem(
    key: ArgumentKey,
    onSelectKey: (ArgumentKey) -> Unit,
) {
    val description = key.toDescription() ?: return
    Row(
        Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(50))
            .clickable {
                onSelectKey(key)
            }.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(description),
            style = EscaperTheme.typography.bodyLarge,
            color = EscaperTheme.colors.mainText
        )
    }
}

internal fun ArgumentKey.toDescription(): StringResource? = when (this) {
    ArgumentKey.TpwsDebugModeArg -> EscaperRes.string.tpws_debug_mode_description
    ArgumentKey.DryRunArg -> EscaperRes.string.dry_run_flag_description
    ArgumentKey.VersionArg -> EscaperRes.string.version_flag_description
    ArgumentKey.DaemonArg -> EscaperRes.string.daemon_flag_description
    ArgumentKey.PidFileArg -> EscaperRes.string.pidfile_arg_description
    ArgumentKey.UserArg -> EscaperRes.string.user_arg_description
    ArgumentKey.UidArg -> EscaperRes.string.uid_arg_description
    ArgumentKey.BindAddrArg -> EscaperRes.string.bind_addr_arg_description
    ArgumentKey.BindLinkLocalArg -> EscaperRes.string.bind_linklocal_arg_description
    ArgumentKey.BindIface4Arg -> EscaperRes.string.bind_iface4_arg_description
    ArgumentKey.BindIface6Arg -> EscaperRes.string.bind_iface6_arg_description
    ArgumentKey.NewArg -> null
}