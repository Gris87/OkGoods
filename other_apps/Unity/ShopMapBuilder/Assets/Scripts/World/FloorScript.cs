using UnityEngine;

using Utils;
using World.Common;
using World.Objects;



namespace World
{
    /// <summary>
    /// Floor script.
    /// </summary>
    public class FloorScript : ObjectWithProperties
    {
        /// <summary>
        /// The floor identifier.
        /// </summary>
        public int floorId;



        /// <summary>
        /// Script starting callback.
        /// </summary>
        protected override void Start()
        {
            base.Start();

            DebugEx.Verbose("FloorScript.Start()");

            GenerateFloor();
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

                    FloorTileScript floorTile = tileObject.AddComponent<FloorTileScript>();
                    floorTile.parent = this;
                }
            }
        }
    }
}
