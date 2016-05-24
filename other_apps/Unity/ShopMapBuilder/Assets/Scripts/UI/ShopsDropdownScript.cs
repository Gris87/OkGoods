using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityTranslation;

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
            mDropdown.AddOptions(CodeManager.shops);

            Translator.AddLanguageChangedListener(OnLanguageChanged);
        }

        /// <summary>
        /// Raises the language changed event.
        /// </summary>
        public void OnLanguageChanged()
        {
            CodeManager.UpdateShopsInfo();

            mDropdown.ClearOptions();
            mDropdown.AddOptions(CodeManager.shops);
        }
    }
}
