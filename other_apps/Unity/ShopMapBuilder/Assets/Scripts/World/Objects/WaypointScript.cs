using UnityEngine;

using Utils;
using World.Common;



namespace World.Objects
{
    /// <summary>
    /// Waypoint script.
    /// </summary>
    public class WaypointScript : SelectableObject
    {
        /// <summary>
        /// Script starting callback.
        /// </summary>
        protected override void Start()
        {
            base.Start();

            DebugEx.VeryVeryVerbose("WaypointScript.Start()");
        }
    }
}
