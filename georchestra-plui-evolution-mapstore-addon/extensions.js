import { createPlugin } from "@mapstore/utils/PluginsUtils";

export default {
	pluievolution: createPlugin('pluievolution', {
        lazy: true,
        loader: () => import(/* webpackChunkName: "extensions/extension" */`./plugins/PluiEvolutionExtension`)
    })
};
