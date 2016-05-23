using UnityEngine;



namespace Cameras
{
    public class MainCameraScript : MonoBehaviour
    {
        // Use this for initialization
        void Start()
        {
        }

        // Update is called once per frame
        void Update()
        {
            Vector3 moveDirection = transform.TransformDirection(new Vector3(InputControl.GetAxis(Controls.axes.horizontal), 0, InputControl.GetAxis(Controls.axes.vertical)));
            moveDirection.y = 0;

            if (InputControl.GetButton(Controls.buttons.run))
            {
                moveDirection = moveDirection * Constants.MAIN_CAMERA_RUN_SPEED * Time.deltaTime;
            }
            else
            {
                moveDirection = moveDirection * Constants.MAIN_CAMERA_SPEED     * Time.deltaTime;
            }

            transform.position = transform.position + moveDirection;
        }
    }
}
