using System.Collections.ObjectModel;
using UnityEngine;

using Utils;



/// <summary>
/// <see cref="Controls"/> is a set of user defined buttons and axes. It is better to store this file somewhere in your project.
/// </summary>
public static class Controls
{
    /// <summary>
    /// <see cref="Buttons"/> is a set of user defined buttons.
    /// </summary>
    public struct Buttons
    {
        public KeyMapping up;
        public KeyMapping down;
        public KeyMapping left;
        public KeyMapping right;
        public KeyMapping select;
        public KeyMapping jump;
        public KeyMapping run;
        public KeyMapping lookUp;
        public KeyMapping lookDown;
        public KeyMapping lookLeft;
        public KeyMapping lookRight;
    }

    /// <summary>
    /// <see cref="Axes"/> is a set of user defined axes.
    /// </summary>
    public struct Axes
    {
        public Axis vertical;
        public Axis horizontal;
        public Axis mouseX;
        public Axis mouseY;
    }



    /// <summary>
    /// Set of buttons.
    /// </summary>
    public static Buttons buttons;

    /// <summary>
    /// Set of axes.
    /// </summary>
    public static Axes axes;



    /// <summary>
    /// Initializes the <see cref="Controls"/> class.
    /// </summary>
    static Controls()
    {
        DebugEx.Verbose("Static class Controls initialized");

        buttons.up        = InputControl.SetKey("Up",        KeyCode.W,            KeyCode.UpArrow,     new JoystickInput(JoystickAxis.Axis2Negative));
        buttons.down      = InputControl.SetKey("Down",      KeyCode.S,            KeyCode.DownArrow,   new JoystickInput(JoystickAxis.Axis2Positive));
        buttons.left      = InputControl.SetKey("Left",      KeyCode.A,            KeyCode.LeftArrow,   new JoystickInput(JoystickAxis.Axis1Negative));
        buttons.right     = InputControl.SetKey("Right",     KeyCode.D,            KeyCode.RightArrow,  new JoystickInput(JoystickAxis.Axis1Positive));
        buttons.select    = InputControl.SetKey("Select",    MouseButton.Left,     KeyCode.LeftControl, new JoystickInput(JoystickButton.Button1));
        buttons.jump      = InputControl.SetKey("Jump",      KeyCode.Space,        KeyCode.None,        new JoystickInput(JoystickButton.Button4));
        buttons.run       = InputControl.SetKey("Run",       KeyCode.LeftShift,    KeyCode.RightShift,  new JoystickInput(JoystickButton.Button5));
        buttons.lookUp    = InputControl.SetKey("LookUp",    MouseAxis.MouseUp,    KeyCode.None,        new JoystickInput(JoystickAxis.Axis4Negative));
        buttons.lookDown  = InputControl.SetKey("LookDown",  MouseAxis.MouseDown,  KeyCode.None,        new JoystickInput(JoystickAxis.Axis4Positive));
        buttons.lookLeft  = InputControl.SetKey("LookLeft",  MouseAxis.MouseLeft,  KeyCode.None,        new JoystickInput(JoystickAxis.Axis3Negative));
        buttons.lookRight = InputControl.SetKey("LookRight", MouseAxis.MouseRight, KeyCode.None,        new JoystickInput(JoystickAxis.Axis3Positive));

        axes.vertical     = InputControl.SetAxis("Vertical",   buttons.down,     buttons.up);
        axes.horizontal   = InputControl.SetAxis("Horizontal", buttons.left,     buttons.right);
        axes.mouseX       = InputControl.SetAxis("Mouse X",    buttons.lookDown, buttons.lookUp);
        axes.mouseY       = InputControl.SetAxis("Mouse Y",    buttons.lookLeft, buttons.lookRight);

        Load();
    }

    /// <summary>
    /// Nothing. It just call static constructor if needed.
    /// </summary>
    public static void Init()
    {
        DebugEx.Verbose("Controls.Init()");

        // Nothing. It just call static constructor if needed
    }

    /// <summary>
    /// Save controls.
    /// </summary>
    public static void Save()
    {
        DebugEx.Verbose("Controls.Save()");

        // It is just an example. You may remove it or modify it if you want
        ReadOnlyCollection<KeyMapping> keys = InputControl.GetKeys();

        foreach (KeyMapping key in keys)
        {
            PlayerPrefs.SetString("Controls." + key.name + ".primary",   key.primaryInput.ToString());
            PlayerPrefs.SetString("Controls." + key.name + ".secondary", key.secondaryInput.ToString());
            PlayerPrefs.SetString("Controls." + key.name + ".third",     key.thirdInput.ToString());
        }

        PlayerPrefs.Save();
    }

    /// <summary>
    /// Load controls.
    /// </summary>
    public static void Load()
    {
        DebugEx.Verbose("Controls.Load()");

        // It is just an example. You may remove it or modify it if you want
        ReadOnlyCollection<KeyMapping> keys = InputControl.GetKeys();

        foreach (KeyMapping key in keys)
        {
            string inputStr;

            inputStr = PlayerPrefs.GetString("Controls." + key.name + ".primary");

            if (inputStr != "")
            {
                key.primaryInput = CustomInputFromString(inputStr);
            }

            inputStr = PlayerPrefs.GetString("Controls." + key.name + ".secondary");

            if (inputStr != "")
            {
                key.secondaryInput = CustomInputFromString(inputStr);
            }

            inputStr = PlayerPrefs.GetString("Controls." + key.name + ".third");

            if (inputStr != "")
            {
                key.thirdInput = CustomInputFromString(inputStr);
            }
        }
    }

    /// <summary>
    /// Converts string representation of CustomInput to CustomInput.
    /// </summary>
    /// <returns>CustomInput from string.</returns>
    /// <param name="value">String representation of CustomInput.</param>
    private static CustomInput CustomInputFromString(string value)
    {
        DebugEx.VerboseFormat("Controls.CustomInputFromString(value = {0})", value);

        CustomInput res;

        res = JoystickInput.FromString(value);

        if (res != null)
        {
            return res;
        }

        res = MouseInput.FromString(value);

        if (res != null)
        {
            return res;
        }

        res = KeyboardInput.FromString(value);

        if (res != null)
        {
            return res;
        }

        return null;
    }
}
