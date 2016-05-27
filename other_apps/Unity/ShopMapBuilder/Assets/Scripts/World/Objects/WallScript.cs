using UnityEngine;

using Utils;
using World.Common;



namespace World.Objects
{
    /// <summary>
    /// Wall script.
    /// </summary>
    public class WallScript : SelectableObject
    {
        /// <summary>
        /// Script starting callback.
        /// </summary>
        protected override void Start()
        {
            base.Start();

            DebugEx.VeryVeryVerbose("WallScript.Start()");
        }
    }
}
