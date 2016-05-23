using UnityEngine;



namespace UI
{
    public class FloorScript : MonoBehaviour
    {
        public int floorId;

        private Mesh     mPlaneMesh;
        private Material mGrassMaterial;
        private Material mFloorMaterial;



        // Use this for initialization
        void Start()
        {
            GeneratePlaneMesh();
            GetMaterials();
            GenerateFloor();
        }

        private void GeneratePlaneMesh()
        {
            mPlaneMesh = new Mesh();
            mPlaneMesh.name = "Plane";

            mPlaneMesh.vertices = new Vector3[4]
            {
                new Vector3(-Constants.TILES_COUNT / 2, 0, -Constants.TILES_COUNT / 2)
                , new Vector3(Constants.TILES_COUNT  / 2, 0, -Constants.TILES_COUNT / 2)
                , new Vector3(-Constants.TILES_COUNT / 2, 0, Constants.TILES_COUNT  / 2)
                , new Vector3(Constants.TILES_COUNT  / 2, 0, Constants.TILES_COUNT  / 2)
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

        private void GetMaterials()
        {
            mGrassMaterial = Resources.Load<Material>("Materials/Grass");
            mFloorMaterial = Resources.Load<Material>("Materials/Floor");
        }

        private void GenerateFloor()
        {
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
