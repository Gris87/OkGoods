using UnityEngine;



public class MainScript : MonoBehaviour
{
	// Use this for initialization
	void Start()
	{
		GameObject floorsObject = new GameObject("Floors");
		floorsObject.transform.SetParent(transform);

		floorsObject.AddComponent<FloorManagementScript>();
	}
}
