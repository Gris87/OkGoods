using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;

using Other;
using Utils;
using World.Common;
using World.Objects;



namespace UI.Toolbox
{
    public class CustomToolButtonScript : MonoBehaviour
    {
        private static ColorBlock sNormalColorBlock;
        private static ColorBlock sSelectedColorBlock;



        private ToolboxScript mToolboxScript;
        private Button        mButton;
        private bool          mActivated;
        private bool          mSomethingSelected;
        private bool          mSelectionMode;



        /// <summary>
        /// Script starting callback.
        /// </summary>
        protected virtual void Start()
        {
            DebugEx.VeryVerbose("CustomToolButtonScript.Start()");

            mToolboxScript = transform.parent.GetComponent<ToolboxScript>();
        }

        /// <summary>
        /// Update is called once per frame.
        /// </summary>
        void Update()
        {
            DebugEx.VeryVeryVerbose("CustomToolButtonScript.Update()");

            if (mActivated)
            {
                if (InputControl.GetMouseButtonDown(MouseButton.Left))
                {
                    if (mSomethingSelected)
                    {
                        OnLeftMouseButtonDown();
                    }
                }
                else
                if (InputControl.GetMouseButtonUp(MouseButton.Left))
                {
                    if (mSomethingSelected)
                    {
                        OnLeftMouseButtonUp();
                    }
                }
                else
                if (mSelectionMode || !EventSystem.current.IsPointerOverGameObject())
                {
                    bool somethingSelected = false;

                    List<RaycastHit> hits = new List<RaycastHit>();
                    Mouse.RaycastAll(hits);

                    foreach (RaycastHit hit in hits)
                    {
                        if (hit.transform.position.y == Global.mainCamera.transform.position.y - Constants.MAIN_CAMERA_HEIGHT * Constants.GLOBAL_SCALE)
                        {
                            SelectableObject selectableObject = hit.transform.GetComponent<SelectableObject>();

                            if (selectableObject != null)
                            {
                                if (!somethingSelected)
                                {
                                    OnObjectSelected(selectableObject);

                                    somethingSelected = true;
                                }
                                        
                                if (selectableObject is FloorTileScript)
                                {
                                    OnFloorSelected((FloorTileScript)selectableObject);

                                    break;
                                }
                            }
                        }
                    }

                    if (!somethingSelected)
                    {
                        OnNothingSelected();
                    }
                }
                else
                {
                    OnNothingSelected();
                }
            }
        }

        /// <summary>
        /// Raises the button clicked event.
        /// </summary>
        public virtual void OnButtonClicked()
        {
            DebugEx.VeryVerbose("CustomToolButtonScript.OnButtonClicked()");

            mToolboxScript.SelectButton(this);
        }

        /// <summary>
        /// Raises the acivate event.
        /// </summary>
        public virtual void OnAcivate()
        {
            DebugEx.VeryVerbose("CustomToolButtonScript.OnAcivate()");

            mActivated = true;

            if (sSelectedColorBlock.colorMultiplier != 1f)
            {
                sSelectedColorBlock.normalColor      = new Color(0.5f, 0.5f, 1f,   1f);
                sSelectedColorBlock.highlightedColor = new Color(0.4f, 0.4f, 0.9f, 1f);
                sSelectedColorBlock.pressedColor     = new Color(0.2f, 0.2f, 0.7f, 1f);
                sSelectedColorBlock.disabledColor    = new Color(0.2f, 0.2f, 0.7f, 0.5f);
                sSelectedColorBlock.colorMultiplier  = 1f;
                sSelectedColorBlock.fadeDuration     = 0.1f;
            }

            GetButton().colors = sSelectedColorBlock;

            mSomethingSelected = false;
            mSelectionMode     = false;
        }

        /// <summary>
        /// Raises the deacivate event.
        /// </summary>
        public virtual void OnDeacivate()
        {
            DebugEx.VeryVerbose("CustomToolButtonScript.OnDeacivate()");

            mActivated = false;

            if (sNormalColorBlock.colorMultiplier != 1f)
            {
                sNormalColorBlock.normalColor      = new Color(1f,   1f,   1f,   1f);
                sNormalColorBlock.highlightedColor = new Color(0.9f, 0.9f, 0.9f, 1f);
                sNormalColorBlock.pressedColor     = new Color(0.7f, 0.7f, 0.7f, 1f);
                sNormalColorBlock.disabledColor    = new Color(0.7f, 0.7f, 0.7f, 0.5f);
                sNormalColorBlock.colorMultiplier  = 1f;
                sNormalColorBlock.fadeDuration     = 0.1f;
            }

            GetButton().colors = sNormalColorBlock;

            mSomethingSelected = false;
            mSelectionMode     = false;
        }

        /// <summary>
        /// Raises the floor selected event.
        /// </summary>
        /// <param name="selectedFloorTile">Selected floor tile.</param>
        protected virtual void OnFloorSelected(FloorTileScript selectedFloorTile)
        {
            DebugEx.VeryVeryVerboseFormat("CustomToolButtonScript.OnFloorSelected(floorTile = {0})", selectedFloorTile);

            mSomethingSelected = true;
        }

        /// <summary>
        /// Raises the object selected event.
        /// </summary>
        /// <param name="selectedObject">Selected object.</param>
        protected virtual void OnObjectSelected(SelectableObject selectedObject)
        {
            DebugEx.VeryVeryVerboseFormat("CustomToolButtonScript.OnObjectSelected(selectedObject = {0})", selectedObject);

            mSomethingSelected = true;
        }

        /// <summary>
        /// Raises the nothing selected event.
        /// </summary>
        protected virtual void OnNothingSelected()
        {
            DebugEx.VeryVeryVerbose("CustomToolButtonScript.OnNothingSelected()");

            if (!mSelectionMode)
            {
                mSomethingSelected = false;
            }
        }

        /// <summary>
        /// Raises the left mouse button down event.
        /// </summary>
        protected virtual void OnLeftMouseButtonDown()
        {
            DebugEx.VeryVerbose("CustomToolButtonScript.OnLeftMouseButtonDown()");

            mSelectionMode = true;
        }

        /// <summary>
        /// Raises the left mouse button up event.
        /// </summary>
        protected virtual void OnLeftMouseButtonUp()
        {
            DebugEx.VeryVerbose("CustomToolButtonScript.OnLeftMouseButtonUp()");

            mSelectionMode     = false;
            mSomethingSelected = false;
        }

        /// <summary>
        /// Gets the button.
        /// </summary>
        /// <returns>The button.</returns>
        private Button GetButton()
        {
            DebugEx.VeryVerbose("CustomToolButtonScript.GetButton()");

            if (mButton == null)
            {
                mButton = GetComponent<Button>();
            }

            return mButton;
        }
    }
}
