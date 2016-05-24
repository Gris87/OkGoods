using UnityEngine;

using Other;



namespace Cameras
{
    /// <summary>
    /// Main camera script.
    /// </summary>
    public class MainCameraScript : MonoBehaviour
    {
        // Use this for initialization
        void Start()
        {
        }

        // Update is called once per frame
        void Update()
        {
            Vector3 moveDirection = new Vector3(InputControl.GetAxis(Controls.axes.horizontal), 0, InputControl.GetAxis(Controls.axes.vertical));

            if (
                !InputControl.GetMouseButton(MouseButton.Left)
                &&
                !InputControl.GetMouseButton(MouseButton.Right)
                &&
                !InputControl.GetMouseButton(MouseButton.Middle)
               )
            {
                float mouseX = Mouse.x;
                float mouseY = Mouse.y;

                if (mouseX < 20)
                {
                    moveDirection.x -= 1;
                }
                else
                if (mouseX > Screen.width - 20)
                {
                    moveDirection.x += 1;
                }

                if (mouseY < 20)
                {
                    moveDirection.z += 1;
                }
                else
                if (mouseY > Screen.height - 20)
                {
                    moveDirection.z -= 1;
                }
            }

            moveDirection = transform.TransformDirection(moveDirection);
            moveDirection.y = 0;

            if (InputControl.GetButton(Controls.buttons.run))
            {
                moveDirection = moveDirection * Constants.MAIN_CAMERA_RUN_SPEED * Constants.GLOBAL_SCALE * Time.deltaTime;
            }
            else
            {
                moveDirection = moveDirection * Constants.MAIN_CAMERA_SPEED     * Constants.GLOBAL_SCALE * Time.deltaTime;
            }

            transform.position = transform.position + moveDirection;
        }
    }
}
