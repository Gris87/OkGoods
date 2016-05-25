using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityTranslation;

using Other;
using Utils;



namespace UI
{
    /// <summary>
    /// Shops dropdown script.
    /// </summary>
    public class ShopsDropdownScript : MonoBehaviour
    {
        private Dropdown mDropdown;



        /// <summary>
        /// Script starting callback.
        /// </summary>
        void Start()
        {
            DebugEx.Verbose("ShopsDropdownScript.Start()");

            mDropdown = GetComponent<Dropdown>();
            mDropdown.AddOptions(CodeManager.shops);

            Translator.AddLanguageChangedListener(OnLanguageChanged);
        }

        /// <summary>
        /// Raises the language changed event.
        /// </summary>
        public void OnLanguageChanged()
        {
            DebugEx.Verbose("ShopsDropdownScript.OnLanguageChanged()");

            CodeManager.UpdateShopsInfo();

            mDropdown.ClearOptions();
            mDropdown.AddOptions(CodeManager.shops);
        }
    }
}
