import {isPluievolutionActivate} from "@js/extension/selectors/plui-evolution-selector";
import {actions} from '../actions/plui-evolution-action';

export function logEvent(action$, store){
    return action$.ofType(actions.PLUI_EVOLUTION_DISPLAY_LOG)
        .filter((action) => isPluievolutionActivate(store.getState()))
        .switchMap((action) => {
            // On pourra par la suite ajouter une condition pour voir si les log sont autorisés
            if (action?.logMessage) {
                console.log(action.logMessage);
            }
        })
}
