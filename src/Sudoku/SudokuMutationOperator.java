package Sudoku;

import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.ICompositeGene;
import org.jgap.IGeneticOperatorConstraint;
import org.jgap.IUniversalRateCalculator;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.RandomGenerator;
import org.jgap.data.config.Configurable;
import org.jgap.impl.CompositeGene;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.MutationOperator;


public class SudokuMutationOperator extends MutationOperator implements Configurable{
	
	  private static final Random ran = new Random();
	  private IUniversalRateCalculator m_mutationRateCalc;

	  private MutationOperatorConfigurable m_config = MutationOperatorConfigurable(); 
	  
	public SudokuMutationOperator() throws InvalidConfigurationException {
		// TODO Auto-generated constructor stub
		super();
	}
	public SudokuMutationOperator(Configuration conf, int mutation_rate) throws InvalidConfigurationException{
		super(conf,mutation_rate);
	}
	
	public void operate(final Population a_population,  final List a_candidateChromosomes) {
	if (a_population == null || a_candidateChromosomes == null) {
		// Population or candidate chromosomes list empty:
		// nothing to do.
		// -----------------------------------------------
		return;
	}
	if (m_config.m_mutationRate == 0 && m_mutationRateCalc == null) {
		// If the mutation rate is set to zero and dynamic mutation rate is
		// disabled, then we don't perform any mutation.
		// ----------------------------------------------------------------
		return;
	}
	// Determine the mutation rate. If dynamic rate is enabled, then
	// calculate it using the IUniversalRateCalculator instance.
	// Otherwise, go with the mutation rate set upon construction.
	// -------------------------------------------------------------
	boolean mutate = false;
	RandomGenerator generator = getConfiguration().getRandomGenerator();
	// It would be inefficient to create copies of each Chromosome just
	// to decide whether to mutate them. Instead, we only make a copy
	// once we've positively decided to perform a mutation.
	// ----------------------------------------------------------------
	int size = Math.min(getConfiguration().getPopulationSize(),
	                a_population.size());
	IGeneticOperatorConstraint constraint = getConfiguration().
	getJGAPFactory().getGeneticOperatorConstraint();
	//
	for (int i = 0; i < size; i++) {
		IChromosome chrom = a_population.getChromosome(i);
		Gene[] genes1 = chrom.getGenes();
		IChromosome copyOfChromosome = null;
		Gene[] genes = null;
		// For each Chromosome in the population...
		// ----------------------------------------
		for (int j = 0; j < genes1.length; j++) {
			if (m_mutationRateCalc != null) {
				// If it's a dynamic mutation rate then let the calculator decide
				// whether the current gene should be mutated.
				// --------------------------------------------------------------
				mutate = m_mutationRateCalc.toBePermutated(chrom, j);
			}
			else {
				// Non-dynamic, so just mutate based on the the current rate.
				// In fact we use a rate of 1/m_mutationRate.
				// ----------------------------------------------------------
				mutate = (generator.nextInt(m_config.m_mutationRate) == 0);
			}
			if (mutate) {
				// Verify that crossover allowed.
				// ------------------------------
				/**@todo move to base class, refactor*/
				if (constraint != null) {
				    List v = new Vector();
				    v.add(chrom);
				    if (!constraint.isValid(a_population, v, this)) {
				    	continue;
				    }
				}
				// Now that we want to actually modify the Chromosome,
				// let's make a copy of it (if we haven't already) and
				// add it to the candidate chromosomes so that it will
				// be considered for natural selection during the next
				// phase of evolution. Then we'll set the gene's value
				// to a random value as the implementation of our
				// "mutation" of the gene.
				// ---------------------------------------------------
				if (copyOfChromosome == null) {
    				// ...take a copy of it...
    				// -----------------------
    				copyOfChromosome = (IChromosome) chrom.clone();
    				// ...add it to the candidate pool...
    				// ----------------------------------
    				a_candidateChromosomes.add(copyOfChromosome);
    				// ...then mutate all its genes...
    				// -------------------------------
    				genes = copyOfChromosome.getGenes();
    				// In case monitoring is active, support it.
    				// -----------------------------------------
   					if (m_monitorActive) {
      					copyOfChromosome.setUniqueIDTemplate(chrom.getUniqueID(), 1);
    				}
  				}
  					// Process all atomic elements in the gene. For a StringGene this
  					// would be as many elements as the string is long , for an
  					// IntegerGene, it is always one element.
  					// --------------------------------------------------------------
  					if (genes[j] instanceof ICompositeGene) {
    					ICompositeGene compositeGene = (ICompositeGene) genes[j];
    					if (m_monitorActive) {
      						compositeGene.setUniqueIDTemplate(chrom.getGene(j).getUniqueID(), 1);
    					}
    					for (int k = 0; k < compositeGene.size(); k++) {
      						mutateGene(compositeGene.geneAt(k));
      						if (m_monitorActive) {
        						compositeGene.geneAt(k).setUniqueIDTemplate(
            					( (ICompositeGene) chrom.getGene(j)).geneAt(k).getUniqueID(),
           						1);
     						}
    					}
 	 				}
  					else {
    					mutateGene(genes[j]);
    					if (m_monitorActive) {
      					genes[j].setUniqueIDTemplate(chrom.getGene(j).getUniqueID(), 1);
    				}
  				}
			}
		}
	}
}

	private void mutateGene(Gene genes) {
		int rand1 = ran.nextInt(genes.size()-1);
		int rand2 = rand1;
		while(rand1 == rand2)
			rand2 = ran.nextInt(genes.size()-1);
		Vector<Integer> aux;
		aux = new Vector<Integer>((Vector<Integer>) genes.getAllele());
		int temp = aux.get(rand1);
		aux.set(rand1, aux.get(rand2));
		aux.set(rand2, temp);
		genes.setAllele(aux);
	}
}


