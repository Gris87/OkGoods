using UnityEngine;



namespace UI
{
    public class MasterScript : MonoBehaviour
    {
        // Use this for initialization
        void Start()
        {
            GameObject floorsObject = new GameObject("Floors");
            floorsObject.transform.SetParent(transform);

            floorsObject.AddComponent<FloorManagementScript>();
        }
    }
}
