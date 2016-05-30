using UnityEngine;

using Utils;



namespace World.Common
{
    /// <summary>
    /// Selectable object.
    /// </summary>
    public class SelectableObject : ObjectWithProperties
    {
        private Renderer mRenderer;
        private Material mSelectedMaterial;
        private Material mDeselectedMaterial;
        private bool     mSelected;



        /// <summary>
        /// Script starting callback.
        /// </summary>
        protected override void Start()
        {
            base.Start();

            DebugEx.VeryVeryVerbose("SelectableObject.Start()");

            mRenderer           = null;
            mSelectedMaterial   = null;
            mDeselectedMaterial = null;
            mSelected           = false;
        }

        /// <summary>
        /// Select this instance.
        /// </summary>
        public void Select()
        {
            DebugEx.VeryVeryVerbose("SelectableObject.Select()");

            if (!mSelected)
            {
                mSelected = true;

                Invalidate();
            }
        }

        /// <summary>
        /// Deselect this instance.
        /// </summary>
        public void Deselect()
        {
            DebugEx.VeryVeryVerbose("SelectableObject.Deselect()");

            if (mSelected)
            {
                mSelected = false;

                Invalidate();
            }
        }

        /// <summary>
        /// Sets the renderer.
        /// </summary>
        /// <param name="renderer">Renderer.</param>
        protected void SetRenderer(Renderer renderer)
        {
            DebugEx.VeryVeryVerboseFormat("SelectableObject.SetRenderer(renderer = {0})", renderer);

            mRenderer = renderer;
        }

        /// <summary>
        /// Sets the selected material.
        /// </summary>
        /// <param name="material">Material.</param>
        protected void SetSelectedMaterial(Material material)
        {
            DebugEx.VeryVeryVerboseFormat("SelectableObject.SetSelectedMaterial(material = {0})", material);

            mSelectedMaterial = material;

            if (mSelected)
            {
                Invalidate();
            }
        }

        /// <summary>
        /// Sets the deselected material.
        /// </summary>
        /// <param name="material">Material.</param>
        protected void SetDeselectedMaterial(Material material)
        {
            DebugEx.VeryVeryVerboseFormat("SelectableObject.SetDeselectedMaterial(material = {0})", material);

            mDeselectedMaterial = material;

            if (!mSelected)
            {
                Invalidate();
            }
        }

        /// <summary>
        /// Invalidate this instance.
        /// </summary>
        protected virtual void Invalidate()
        {
            DebugEx.VeryVeryVerbose("SelectableObject.Invalidate()");

            if (mSelected)
            {
                mRenderer.material = mSelectedMaterial;
            }
            else
            {
                mRenderer.material = mDeselectedMaterial;
            }
        }
    }
}
