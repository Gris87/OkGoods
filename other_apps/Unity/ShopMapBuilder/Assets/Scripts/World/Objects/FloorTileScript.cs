using UnityEngine;

using Utils;
using World.Common;



namespace World.Objects
{
    /// <summary>
    /// Floor tile script.
    /// </summary>
    public class FloorTileScript : SelectableObject
    {
        /// <summary>
        /// Script starting callback.
        /// </summary>
        protected override void Start()
        {
            base.Start();

            DebugEx.VeryVeryVerbose("FloorTileScript.Start()");
        }
    }
}
