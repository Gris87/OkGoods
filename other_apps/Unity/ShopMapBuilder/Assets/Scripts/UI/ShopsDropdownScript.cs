using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;



namespace UI
{
    /// <summary>
    /// Shops dropdown script.
    /// </summary>
    public class ShopsDropdownScript : MonoBehaviour
    {
        private Dropdown mDropdown;



        // Use this for initialization
        void Start()
        {
            mDropdown = GetComponent<Dropdown>();

            List<string> shops = new List<string>();
            shops.Add("Hello");
            shops.Add("World");

            mDropdown.AddOptions(shops);
        }
    }
}
