using UnityEngine;

using Utils;



namespace World
{
    /// <summary>
    /// Floor script.
    /// </summary>
    public class FloorScript : MonoBehaviour
    {
        /// <summary>
        /// The floor identifier.
        /// </summary>
        public int floorId;



        private Mesh     mPlaneMesh;
        private Material mGrassMaterial;
        private Material mFloorMaterial;



        /// <summary>
        /// Script starting callback.
        /// </summary>
        void Start()
        {
            DebugEx.Verbose("FloorScript.Start()");

            GeneratePlaneMesh();
            GetMaterials();
            GenerateFloor();
        }

        /// <summary>
        /// Generates the plane mesh.
        /// </summary>
        private void GeneratePlaneMesh()
        {
            DebugEx.Verbose("FloorScript.GeneratePlaneMesh()");

            mPlaneMesh = new Mesh();
            mPlaneMesh.name = "Plane";

            mPlaneMesh.vertices = new Vector3[4]
            {
                  new Vector3(-Constants.GLOBAL_SCALE / 2, 0, -Constants.GLOBAL_SCALE / 2)
                , new Vector3(Constants.GLOBAL_SCALE  / 2, 0, -Constants.GLOBAL_SCALE / 2)
                , new Vector3(-Constants.GLOBAL_SCALE / 2, 0, Constants.GLOBAL_SCALE  / 2)
                , new Vector3(Constants.GLOBAL_SCALE  / 2, 0, Constants.GLOBAL_SCALE  / 2)
            };

            mPlaneMesh.triangles = new int[]
            {
                  0
                , 2
                , 1

                , 2
                , 3
                , 1
            };

            mPlaneMesh.normals = new Vector3[]
            {
                  new Vector3(0, 1, 0)
                , new Vector3(0, 1, 0)
                , new Vector3(0, 1, 0)
                , new Vector3(0, 1, 0)
            };

            mPlaneMesh.uv = new Vector2[]
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

            mGrassMaterial = Resources.Load<Material>("Materials/Grass");
            mFloorMaterial = Resources.Load<Material>("Materials/Floor");
        }

        /// <summary>
        /// Generates floor tiles.
        /// </summary>
        private void GenerateFloor()
        {
            DebugEx.Verbose("FloorScript.GenerateFloor()");

            for (int i = 0; i < Constants.TILES_COUNT; ++i)
            {
                for (int j = 0; j < Constants.TILES_COUNT; ++j)
                {
                    GameObject tileObject = new GameObject("Tile_" + i + "_" + j);
                    tileObject.transform.SetParent(transform);
                    tileObject.transform.position = new Vector3((i - Constants.TILES_COUNT / 2) * Constants.GLOBAL_SCALE, 0, (j - Constants.TILES_COUNT / 2) * Constants.GLOBAL_SCALE);

                    MeshFilter   meshFilter   = tileObject.AddComponent<MeshFilter>();
                    MeshCollider meshCollider = tileObject.AddComponent<MeshCollider>();
                    MeshRenderer meshRenderer = tileObject.AddComponent<MeshRenderer>();

                    meshFilter.mesh         = mPlaneMesh;
                    meshCollider.sharedMesh = mPlaneMesh;



                    if (floorId == 1)
                    {
                        meshRenderer.material = mGrassMaterial;
                    }
                    else
                    {
                        meshRenderer.material = mFloorMaterial;
                    }               
                }
            }
        }
    }
}
