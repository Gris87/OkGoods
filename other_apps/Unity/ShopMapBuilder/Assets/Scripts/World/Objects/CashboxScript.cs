using UnityEngine;

using Utils;
using World.Common;



namespace World.Objects
{
    /// <summary>
    /// Cashbox script.
    /// </summary>
    public class CashboxScript : SelectableObject
    {
        /// <summary>
        /// Script starting callback.
        /// </summary>
        protected override void Start()
        {
            base.Start();

            DebugEx.VeryVeryVerbose("CashboxScript.Start()");
        }
    }
}
