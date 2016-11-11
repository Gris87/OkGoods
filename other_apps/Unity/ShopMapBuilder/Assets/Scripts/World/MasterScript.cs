using UnityEngine;

using Utils;



namespace World
{
    /// <summary>
    /// Master script.
    /// </summary>
    public class MasterScript : MonoBehaviour
    {
        /// <summary>
        /// Script starting callback.
        /// </summary>
        void Start()
        {
            DebugEx.Verbose("MasterScript.Start()");

            GameObject floorsObject = new GameObject("Floors");
            floorsObject.transform.SetParent(transform);

            floorsObject.AddComponent<FloorManagementScript>();
        }
    }
}
