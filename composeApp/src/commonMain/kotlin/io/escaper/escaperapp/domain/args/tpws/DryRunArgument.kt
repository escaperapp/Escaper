package io.escaper.escaperapp.domain.args.tpws

import io.escaper.escaperapp.domain.args.ArgumentKey
import io.escaper.escaperapp.domain.args.FlagArgument

internal data object DryRunArgument : FlagArgument(ArgumentKey.DryRunArg)