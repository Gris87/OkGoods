using UnityEngine;



namespace UI
{
    public class FloorManagementScript : MonoBehaviour
    {
        // Use this for initialization
        void Start()
        {
            AddFloor(1);
        }

        public void AddFloor(int floorId)
        {
            GameObject floorObject = new GameObject("Floor " + floorId);
            floorObject.transform.SetParent(transform);

            FloorScript floorScript = floorObject.AddComponent<FloorScript>();

            floorScript.floorId = floorId;
        }
    }
}
