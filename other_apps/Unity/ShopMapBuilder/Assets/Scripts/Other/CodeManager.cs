using System.Collections.Generic;
using System.IO;
using UnityEngine;

using Utils;



namespace Other
{
    /// <summary>
    /// Code manager.
    /// </summary>
    public static class CodeManager
    {
        private static string sProjectDir;



        /// <summary>
        /// Initializes the <see cref="Other.CodeManager"/> class.
        /// </summary>
        static CodeManager()
        {
            DebugEx.Verbose("Static class CodeManager initialized");

            sProjectDir = Application.dataPath;

            do
            {
                DebugEx.DebugFormat("sProjectDir = {0}", sProjectDir);

                if (File.Exists(sProjectDir + "/build.gradle"))
                {
                    break;
                }

                int index = sProjectDir.LastIndexOf('/');

                if (index < 0)
                {
                    DebugEx.Error("Failed to find project directory");

                    break;
                }

                sProjectDir = sProjectDir.Substring(0, index);
            } while(true);
        }

        public static List<string> GetShops()
        {
            List<string> res = new List<string>();

            return res;
        }
    }
}