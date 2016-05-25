using System.Collections.Generic;
using System.IO;
using UnityEngine;
using UnityTranslation;

using Utils;



namespace Other
{
    /// <summary>
    /// Code manager.
    /// </summary>
    public static class CodeManager
    {
        private static string sProjectDir;
        public static List<string> shops;
        public static List<string> shopsIDs;



        /// <summary>
        /// Initializes the <see cref="Other.CodeManager"/> class.
        /// </summary>
        static CodeManager()
        {
            DebugEx.Verbose("Static class CodeManager initialized");

            sProjectDir = Application.dataPath;

            do
            {
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

            UpdateShopsInfo();
        }

        /// <summary>
        /// Updates info about shops.
        /// </summary>
        public static void UpdateShopsInfo()
        {
            DebugEx.Verbose("CodeManager.UpdateShopsInfo()");

            shops    = new List<string>();
            shopsIDs = new List<string>();

            StreamReader reader;
            string language = LanguageCode.LanguageToCode(Translator.language);

            if (File.Exists(sProjectDir + "/app/src/main/res/values-" + language + "/strings.xml"))
            {
                reader = File.OpenText(sProjectDir + "/app/src/main/res/values-" + language + "/strings.xml");
            }
            else
            {
                reader = File.OpenText(sProjectDir + "/app/src/main/res/values-ru/strings.xml");
            }

            while (!reader.EndOfStream)
            {
                string line = reader.ReadLine().Trim();

                if (line.StartsWith("<string name=\"shop_"))
                {
                    int index = line.IndexOf('\"', 19);

                    if (index >= 0)
                    {
                        string shopId = line.Substring(19, index - 19);

                        index = line.IndexOf('>', index + 1);

                        if (index >= 0)
                        {
                            int index2 = line.IndexOf("</string>", index + 1);

                            if (index2 >= 0)
                            {
                                string shop = line.Substring(index + 1, index2 - index - 1).Replace("\\\'", "\'");

                                shops.Add(shop);
                                shopsIDs.Add(shopId);
                            }
                        }
                    }
                }
            }

            reader.Close();
        }
    }
}
