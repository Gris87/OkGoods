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
        private static Material sFloorMaterial;



        public FloorScript parent;



        /// <summary>
        /// Script starting callback.
        /// </summary>
        protected override void Start()
        {
            base.Start();

            DebugEx.VeryVeryVerbose("FloorTileScript.Start()");

            GeneratePlaneMesh();
            GetMaterials();
            Initialize();
        }

        /// <summary>
        /// Generates the plane mesh.
        /// </summary>
        private void GeneratePlaneMesh()
        {
            DebugEx.VeryVerbose("FloorScript.GeneratePlaneMesh()");

            if (sPlaneMesh == null)
            {
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
        }

        /// <summary>
        /// Gets the materials from resources.
        /// </summary>
        private void GetMaterials()
        {
            DebugEx.VeryVerbose("FloorScript.GetMaterials()");

            if (sGrassMaterial == null)
            {
                sGrassMaterial = Resources.Load<Material>("Materials/Grass");
                sFloorMaterial = Resources.Load<Material>("Materials/Floor");
            }
        }

        /// <summary>
        /// Initialize this instance.
        /// </summary>
        private void Initialize()
        {
            MeshFilter   meshFilter   = gameObject.AddComponent<MeshFilter>();
            MeshCollider meshCollider = gameObject.AddComponent<MeshCollider>();
            MeshRenderer meshRenderer = gameObject.AddComponent<MeshRenderer>();

            meshFilter.mesh         = sPlaneMesh;
            meshCollider.sharedMesh = sPlaneMesh;



            if (parent.floorId == 1)
            {
                meshRenderer.material = sGrassMaterial;
            }
            else
            {
                meshRenderer.material = sFloorMaterial;
            }
        }
    }
}
