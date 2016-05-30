using UnityEngine;

using Utils;



namespace World.Common
{
    /// <summary>
    /// Object with properties.
    /// </summary>
    public class ObjectWithProperties : MonoBehaviour
    {
        /// <summary>
        /// Script starting callback.
        /// </summary>
        protected virtual void Start()
        {
            DebugEx.VeryVeryVerbose("ObjectWithProperties.Start()");
        }
    }
}
