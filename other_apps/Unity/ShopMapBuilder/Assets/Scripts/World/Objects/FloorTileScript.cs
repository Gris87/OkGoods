using UnityEngine;

using Utils;
using World;
using World.Common;



namespace World.Objects
{
    /// <summary>
    /// Floor tile script.
    /// </summary>
    public class FloorTileScript : SelectableObject
    {
        private static Mesh     sPlaneMesh;
        private static Material sGrassMaterial;
        private static Material sGrassSelectedMaterial;
        private static Material sFloorMaterial;
        private static Material sFloorSelectedMaterial;



        public FloorScript parent;



        /// <summary>
        /// Script starting callback.
        /// </summary>
        protected override void Start()
        {
            base.Start();

            DebugEx.VeryVeryVerbose("FloorTileScript.Start()");

            StaticInitialize();
            Initialize();
        }

        /// <summary>
        /// Generates the plane mesh.
        /// </summary>
        private void GeneratePlaneMesh()
        {
            DebugEx.Verbose("FloorScript.GeneratePlaneMesh()");

            sPlaneMesh = new Mesh();
            sPlaneMesh.name = "Plane";

            sPlaneMesh.vertices = new Vector3[4]
            {
                  new Vector3(-Constants.GLOBAL_SCALE / 2, 0, -Constants.GLOBAL_SCALE / 2)
                , new Vector3(Constants.GLOBAL_SCALE  / 2, 0, -Constants.GLOBAL_SCALE / 2)
                , new Vector3(-Constants.GLOBAL_SCALE / 2, 0, Constants.GLOBAL_SCALE  / 2)
                , new Vector3(Constants.GLOBAL_SCALE  / 2, 0, Constants.GLOBAL_SCALE  / 2)
            };

            sPlaneMesh.triangles = new int[]
            {
                  0
                , 2
                , 1

                , 2
                , 3
                , 1
            };

            sPlaneMesh.normals = new Vector3[]
            {
                  new Vector3(0, 1, 0)
                , new Vector3(0, 1, 0)
                , new Vector3(0, 1, 0)
                , new Vector3(0, 1, 0)
            };

            sPlaneMesh.uv = new Vector2[]
            {
                  new Vector2(0, 0)
                , new Vector2(1, 0)
                , new Vector2(0, 1)
                , new Vector2(1, 1)
            };
        }

        /// <summary>
        /// Gets the materials from resources.
        /// </summary>
        private void GetMaterials()
        {
            DebugEx.Verbose("FloorScript.GetMaterials()");

            sGrassMaterial         = Resources.Load<Material>("Materials/Grass");
            sGrassSelectedMaterial = Resources.Load<Material>("Materials/GrassSelected");
            sFloorMaterial         = Resources.Load<Material>("Materials/Floor");
            sFloorSelectedMaterial = Resources.Load<Material>("Materials/FloorSelected");
        }

        /// <summary>
        /// Initialize this instance.
        /// </summary>
        private void StaticInitialize()
        {
            DebugEx.VeryVerbose("FloorScript.StaticInitialize()");

            if (sPlaneMesh == null)
            {
                GeneratePlaneMesh();
                GetMaterials();
            }
        }

        /// <summary>
        /// Initialize this instance.
        /// </summary>
        private void Initialize()
        {
            DebugEx.VeryVerbose("FloorScript.Initialize()");

            MeshFilter   meshFilter   = gameObject.AddComponent<MeshFilter>();
            MeshCollider meshCollider = gameObject.AddComponent<MeshCollider>();
            MeshRenderer meshRenderer = gameObject.AddComponent<MeshRenderer>();

            meshFilter.mesh         = sPlaneMesh;
            meshCollider.sharedMesh = sPlaneMesh;
            SetRenderer(meshRenderer);



            if (parent.floorId == 1)
            {
                SetDeselectedMaterial(sGrassMaterial);
                SetSelectedMaterial(sGrassSelectedMaterial);
            }
            else
            {
                SetDeselectedMaterial(sFloorMaterial);
                SetSelectedMaterial(sFloorSelectedMaterial);
            }
        }
    }
}
