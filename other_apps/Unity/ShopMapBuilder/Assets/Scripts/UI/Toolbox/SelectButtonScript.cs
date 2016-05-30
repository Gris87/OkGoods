using UnityEngine;

using Utils;
using World.Common;



namespace UI.Toolbox
{
    /// <summary>
    /// Select button script.
    /// </summary>
    public class SelectButtonScript : CustomToolButtonScript
    {
        private SelectableObject mSelectedObject;



        /// <summary>
        /// Script starting callback.
        /// </summary>
        protected override void Start()
        {
            base.Start();

            DebugEx.Verbose("SelectButtonScript.Start()");

            mSelectedObject = null;
        }

        /// <summary>
        /// Raises the button clicked event.
        /// </summary>
        public override void OnButtonClicked()
        {
            base.OnButtonClicked();

            DebugEx.Verbose("SelectButtonScript.OnButtonClicked()");
        }

        /// <summary>
        /// Raises the object selected event.
        /// </summary>
        /// <param name="selectedObject">Selected object.</param>
        protected override void OnObjectSelected(SelectableObject selectedObject)
        {
            base.OnObjectSelected(selectedObject);

            DebugEx.VeryVerboseFormat("SelectButtonScript.OnObjectSelected(selectedObject = {0})", selectedObject);

            if (mSelectedObject != null)
            {
                mSelectedObject.Deselect();
            }

            mSelectedObject = selectedObject;

            if (mSelectedObject != null)
            {
                mSelectedObject.Select();
            }
        }

        /// <summary>
        /// Raises the nothing selected event.
        /// </summary>
        protected override void OnNothingSelected()
        {
            base.OnNothingSelected();

            DebugEx.VeryVeryVerbose("SelectButtonScript.OnNothingSelected()");

            if (mSelectedObject != null)
            {
                mSelectedObject.Deselect();
            }

            mSelectedObject = null;
        }
    }
}
