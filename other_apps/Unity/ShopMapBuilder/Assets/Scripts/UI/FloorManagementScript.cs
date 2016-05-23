using UnityEngine;

using Utils;



namespace UI
{
    public class FloorManagementScript : MonoBehaviour
    {
        // Use this for initialization
        void Start()
        {
            DebugEx.Verbose("FloorManagementScript.Start()");

            AddFloor(1);
        }

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
