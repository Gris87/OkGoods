using UnityEngine;

using Utils;



namespace UI.Toolbox
{
    /// <summary>
    /// Wall button script.
    /// </summary>
    public class WallButtonScript : CustomToolButtonScript
    {
        /// <summary>
        /// Raises the button clicked event.
        /// </summary>
        public override void OnButtonClicked()
        {
            base.OnButtonClicked();

            DebugEx.Verbose("WallButtonScript.OnButtonClicked()");
        }
    }
}
