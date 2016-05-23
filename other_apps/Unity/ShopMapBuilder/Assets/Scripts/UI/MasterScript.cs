﻿using UnityEngine;

using Utils;



namespace UI
{
    public class MasterScript : MonoBehaviour
    {
        // Use this for initialization
        void Start()
        {
            DebugEx.Verbose("MasterScript.Start()");

            GameObject floorsObject = new GameObject("Floors");
            floorsObject.transform.SetParent(transform);

            floorsObject.AddComponent<FloorManagementScript>();
        }
    }
}
