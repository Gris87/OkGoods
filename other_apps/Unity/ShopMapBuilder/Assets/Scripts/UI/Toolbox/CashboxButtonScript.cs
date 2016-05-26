using UnityEngine;

using Utils;



namespace UI.Toolbox
{
    /// <summary>
    /// Cashbox button script.
    /// </summary>
    public class CashboxButtonScript : CustomToolButtonScript
    {
        /// <summary>
        /// Raises the button clicked event.
        /// </summary>
        public override void OnButtonClicked()
        {
            base.OnButtonClicked();

            DebugEx.Verbose("CashboxButtonScript.OnButtonClicked()");
        }
    }
}
