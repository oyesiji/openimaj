/**
 * Copyright (c) 2011, The University of Southampton and the individual contributors.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *   * 	Redistributions of source code must retain the above copyright notice,
 * 	this list of conditions and the following disclaimer.
 *
 *   *	Redistributions in binary form must reproduce the above copyright notice,
 * 	this list of conditions and the following disclaimer in the documentation
 * 	and/or other materials provided with the distribution.
 *
 *   *	Neither the name of the University of Southampton nor the names of its
 * 	contributors may be used to endorse or promote products derived from this
 * 	software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.openimaj.tools.globalfeature;

import org.kohsuke.args4j.CmdLineOptionsProvider;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.ProxyOptionHandler;
import org.openimaj.feature.FeatureVector;
import org.openimaj.image.FImage;
import org.openimaj.image.MBFImage;
import org.openimaj.image.connectedcomponent.proc.AffineInvariantMoments;
import org.openimaj.image.connectedcomponent.proc.BasicShapeDescriptor;
import org.openimaj.image.connectedcomponent.proc.BoundaryDistanceDescriptor;
import org.openimaj.image.connectedcomponent.proc.ColourDescriptor;
import org.openimaj.image.connectedcomponent.proc.HuMoments;
import org.openimaj.image.connectedcomponent.proc.BasicShapeDescriptor.BasicShapeDescriptorType;
import org.openimaj.image.connectedcomponent.proc.ColourDescriptor.ColourDescriptorType;
import org.openimaj.image.pixel.ConnectedComponent;


/**
 * Features describing the shape of the foreground
 * object in an image. A mask is used to separate
 * foreground/background pixels.
 * 
 * @author Jonathon Hare <jsh2@ecs.soton.ac.uk>
 *
 */
public enum ShapeFeatures implements CmdLineOptionsProvider {
	/**
	 * Any of the {@link GlobalFeatures} applied only to the foreground
	 * region.
	 */
	GLOBAL {
		@Option(name="--global-feature-type", aliases="-g", handler=ProxyOptionHandler.class, usage="Global feature type", required=true)
		private GlobalFeatures feature;
		
		@Override
		public FeatureVector execute(MBFImage image, FImage mask) {
			return feature.execute(image);
		}
	},
	/**
	 * Affine invariant moments
	 * @see AffineInvariantMoments
	 */
	AFFINE_INVARIANT_MOMENTS {
		@Override
		public FeatureVector execute(MBFImage image, FImage mask) {
			AffineInvariantMoments f = new AffineInvariantMoments();
			f.process(c(mask));
			return f.getFeatureVector();
		}
	},
	/**
	 * Basic shape features
	 * @see BasicShapeDescriptor
	 */
	BASIC_SHAPE_FEATURES {
		@Option(name="--type", aliases="-t", usage="Shape feature type", required=true)
		BasicShapeDescriptorType type;
		
		@Override
		public FeatureVector execute(MBFImage image, FImage mask) {
			BasicShapeDescriptor f = new BasicShapeDescriptor();
			f.process(c(mask));
			return type.getFeatureVector(f);
		}
	},
	/**
	 * Distance from boundary descriptor
	 * @see BoundaryDistanceDescriptor
	 */
	BOUNDARY_DISTANCE_FEATURE {
		@Option(name="--no-scale-normalisation", usage="Disable scale normalisation", required=false)
		boolean noNormaliseScale = false;
		
		@Option(name="--no-rotation-normalisation", usage="Disable rotation normalisation", required=false)
		boolean noNormaliseRotation = false;
		
		@Override
		public FeatureVector execute(MBFImage image, FImage mask) {
			BoundaryDistanceDescriptor f = new BoundaryDistanceDescriptor(!noNormaliseScale, !noNormaliseRotation);
			f.process(c(mask));
			return f.getFeatureVector();
		}
	},
	/**
	 * Hu moments
	 * @see HuMoments
	 */
	HU_MOMENTS {
		@Override
		public FeatureVector execute(MBFImage image, FImage mask) {
			HuMoments f = new HuMoments();
			f.process(c(mask));
			return f.getFeatureVector();
		}
	},
	/**
	 * Colour statistics
	 * @see ColourDescriptor
	 */
	COLOR_STATISTICS {
		@Option(name="--type", aliases="-t", usage="Colour statistics type", required=true)
		ColourDescriptorType type;
		
		@Override
		public FeatureVector execute(MBFImage image, FImage mask) {
			ColourDescriptor f = new ColourDescriptor(image);
			f.process(c(mask));
			return type.getFeatureVector(f);
		}
	}
	;

	protected ConnectedComponent c(FImage mask) {
		return new ConnectedComponent(mask, 0.5f);
	}
	
	@Override
	public Object getOptions() {
		return this;
	}
    
    /**
     * Calculate a feature using the shape defined by the
     * mask and possibly the pixel values from the image.
     * @param image the image.
     * @param mask the mask.
     * @return the feature.
     */
    public abstract FeatureVector execute(MBFImage image, FImage mask);
}
