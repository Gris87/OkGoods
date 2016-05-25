using UnityEngine;

using Utils;



namespace World
{
    /// <summary>
    /// Floor management script.
    /// </summary>
    public class FloorManagementScript : MonoBehaviour
    {
        /// <summary>
        /// Script starting callback.
        /// </summary>
        void Start()
        {
            DebugEx.Verbose("FloorManagementScript.Start()");

            AddFloor(1);
        }

        /// <summary>
        /// Adds the floor with specified id.
        /// </summary>
        /// <param name="floorId">Floor identifier.</param>
        public void AddFloor(int floorId)
        {
            DebugEx.VerboseFormat("FloorManagementScript.AddFloor(floorId = {0})", floorId);

            GameObject floorObject = new GameObject("Floor " + floorId);
            floorObject.transform.SetParent(transform);

            FloorScript floorScript = floorObject.AddComponent<FloorScript>();

            floorScript.floorId = floorId;
        }
    }
}
