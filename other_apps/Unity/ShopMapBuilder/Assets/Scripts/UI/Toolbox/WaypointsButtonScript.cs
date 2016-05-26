using UnityEngine;

using Utils;



namespace UI.Toolbox
{
    /// <summary>
    /// Waypoints button script.
    /// </summary>
    public class WaypointsButtonScript : CustomToolButtonScript
    {
        /// <summary>
        /// Raises the button clicked event.
        /// </summary>
        public override void OnButtonClicked()
        {
            base.OnButtonClicked();

            DebugEx.Verbose("WaypointsButtonScript.OnButtonClicked()");
        }
    }
}
