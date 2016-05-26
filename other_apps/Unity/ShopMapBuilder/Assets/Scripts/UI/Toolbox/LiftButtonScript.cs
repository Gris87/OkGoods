using UnityEngine;

using Utils;



namespace UI.Toolbox
{
    /// <summary>
    /// Lift button script.
    /// </summary>
    public class LiftButtonScript : CustomToolButtonScript
    {
        /// <summary>
        /// Raises the button clicked event.
        /// </summary>
        public override void OnButtonClicked()
        {
            base.OnButtonClicked();

            DebugEx.Verbose("LiftButtonScript.OnButtonClicked()");
        }
    }
}
