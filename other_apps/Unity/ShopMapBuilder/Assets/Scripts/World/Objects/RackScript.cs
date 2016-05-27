using UnityEngine;

using Utils;
using World.Common;



namespace World.Objects
{
    /// <summary>
    /// Rack script.
    /// </summary>
    public class RackScript : SelectableObject
    {
        /// <summary>
        /// Script starting callback.
        /// </summary>
        protected override void Start()
        {
            base.Start();

            DebugEx.VeryVeryVerbose("RackScript.Start()");
        }
    }
}
