using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

using Other;



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
            mDropdown.AddOptions(CodeManager.GetShops());
        }
    }
}
