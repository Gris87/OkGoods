using UnityEngine;
using UnityEngine.EventSystems;

using Other;
using Utils;



namespace Cameras
{
    /// <summary>
    /// Main camera script.
    /// </summary>
    public class MainCameraScript : MonoBehaviour
    {
        private float mPreviousMouseX;
        private float mPreviousMouseY;
        private bool  mUIClicked;



        /// <summary>
        /// Script starting callback.
        /// </summary>
        void Start()
        {
            DebugEx.Verbose("MainCameraScript.Start()");

            Global.mainCamera = this;

            mPreviousMouseX = -1f;
            mPreviousMouseY = -1f;
            mUIClicked      = false;

            transform.position = new Vector3(0f, Constants.MAIN_CAMERA_HEIGHT * Constants.GLOBAL_SCALE, 0f);
        }

        /// <summary>
        /// Update is called once per frame.
        /// </summary>
        void Update()
        {
            DebugEx.VeryVeryVerbose("MainCameraScript.Update()");

            float mouseX             = Mouse.x;
            float mouseY             = Mouse.y;
            bool  rightButtonClicked = false;

            if (InputControl.GetMouseButtonDown(MouseButton.Left))
            {
                mUIClicked = EventSystem.current.IsPointerOverGameObject();
            }
            else
            if (InputControl.GetMouseButtonDown(MouseButton.Right))
            {
                rightButtonClicked = true;

                mPreviousMouseX = mouseX;
                mPreviousMouseY = mouseY;
            }
            else
            if (InputControl.GetMouseButtonUp(MouseButton.Left))
            {
                mUIClicked = false;
            }
            else
            if (InputControl.GetMouseButtonUp(MouseButton.Right))
            {
                mPreviousMouseX = -1f;
                mPreviousMouseY = -1f;
            }
            else
            if (InputControl.GetMouseButton(MouseButton.Right))
            {
                rightButtonClicked = true;
            }

            Vector3 moveDirection = new Vector3(InputControl.GetAxis(Controls.axes.horizontal), 0, InputControl.GetAxis(Controls.axes.vertical));

            if (
                !mUIClicked
                &&
                !rightButtonClicked
               )
            {
                if (mouseX < 20f)
                {
                    moveDirection.x -= 1f;
                }
                else
                if (mouseX > Screen.width - 20f)
                {
                    moveDirection.x += 1f;
                }

                if (mouseY < 20f)
                {
                    moveDirection.z += 1f;
                }
                else
                if (mouseY > Screen.height - 20f)
                {
                    moveDirection.z -= 1f;
                }
            }

            moveDirection = transform.TransformDirection(moveDirection);
            moveDirection.y = 0f;

            if (InputControl.GetButton(Controls.buttons.run))
            {
                moveDirection = moveDirection * Constants.MAIN_CAMERA_RUN_SPEED * Constants.GLOBAL_SCALE * Time.deltaTime;
            }
            else
            {
                moveDirection = moveDirection * Constants.MAIN_CAMERA_SPEED     * Constants.GLOBAL_SCALE * Time.deltaTime;
            }

            transform.position = transform.position + moveDirection;



            if (rightButtonClicked)
            {
                Vector3 rotateVector = new Vector3(mouseY - mPreviousMouseY, mouseX - mPreviousMouseX, 0f);

                transform.eulerAngles += rotateVector * Constants.MAIN_CAMERA_ROTATE_SPEED;

                mPreviousMouseX = mouseX;
                mPreviousMouseY = mouseY;
            }
        }
    }
}
