package benchmarkGenerator.questionsGenerator.questionBuilder;

import benchmarkGenerator.questionsGenerator.questionBuilder.helpers.GeneratedQuestion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import benchmarkGenerator.subgraphShapeGenerator.model.TriplePattern;
import benchmarkGenerator.subgraphShapeGenerator.subgraph.StarGraph;
import lexiconGenerator.predicateRepresentationExtractor.chunking.BasicNLP_FromPython;
import settings.Settings;

public class StarQuestionWithGroupBy extends ShapeQuestion {

    StarGraph starGraph;
    String T;
    String T_withPrefix;
    String o;
    String o_type_withPrefix;
    String o_type;
    String p;
    String p_withPrefix;

    ArrayList<GeneratedQuestion> allPossibleQuestions = new ArrayList<>();
//
    public StarQuestionWithGroupBy(StarGraph starGraph) throws Exception {
        this.starGraph = starGraph;
        T = Settings.explorer.removePrefix(starGraph.getSeedType()).toLowerCase();

        Map<String, HashSet<String>> starPredicates = new HashMap<>();

        //Fill starPredicates map to make star as (p1, O1.1, O1.2,...), ..... (P2, O2.1,O2.2,...)
        for (TriplePattern triple : starGraph.getStar()) {
            p = triple.getPredicate().getValue();
            String s = triple.getSubject().getValue();
            o = triple.getObject().getValue();

            if (!starPredicates.containsKey(p)) {
                HashSet<String> objects = new HashSet<>();
                objects.add(o);
                starPredicates.put(p, objects);
            } else {
                starPredicates.get(p).add(o);
            }
            o_type_withPrefix = triple.getO_type();
            o_type = triple.getO_type_without_prefix();
        }
        if (starPredicates.size() == 1 && starPredicates.get(p).size() >= 2) {
            T = Settings.explorer.removePrefix(starGraph.getSeedType()).toLowerCase();
            String question_tagged = "";
            String question = "Which " + T + " has at least 2 " + BasicNLP_FromPython.nounPlural(p.toLowerCase()) + "?";
            question_tagged = "<qt>Which</qt> <t>" + T + "</t> has <op>at least</op> <o>2</o> <p>" + BasicNLP_FromPython.nounPlural(p.toLowerCase()) + "</p>?";
            String query = selectQuery(starGraph, 2, true);
            allPossibleQuestions.add(new GeneratedQuestion(starGraph.getStar().get(0).getSubject().getValueWithPrefix(), starGraph.getStar().get(0).getS_type(), question, question_tagged, query, starGraph.toString(), 2, GeneratedQuestion.QT_WHICH, GeneratedQuestion.SH_STAR_MODIFIED));
            question = "Which " + T + " has at least 6 " + BasicNLP_FromPython.nounPlural(p.toLowerCase()) + "?";
            question_tagged = "<qt>Which</qt> <t>" + T + "</t> has <op>at least</op> <o>6</o> <p>" + BasicNLP_FromPython.nounPlural(p.toLowerCase()) + "</p>?";
            query = selectQuery(starGraph, 6, true);
            allPossibleQuestions.add(new GeneratedQuestion(starGraph.getStar().get(0).getSubject().getValueWithPrefix(), starGraph.getStar().get(0).getS_type(), question, question_tagged, query, starGraph.toString(), 2, GeneratedQuestion.QT_WHICH, GeneratedQuestion.SH_STAR_MODIFIED));
            question = "Which " + T + " has at most 6 " + BasicNLP_FromPython.nounPlural(p.toLowerCase()) + "?";
            question_tagged = "<qt>Which</qt> <t>" + T + "</t> has <op>at most</op> <o>6</o> <p>" + BasicNLP_FromPython.nounPlural(p.toLowerCase()) + "</p>?";
            query = selectQuery(starGraph, 6, false);
            allPossibleQuestions.add(new GeneratedQuestion(starGraph.getStar().get(0).getSubject().getValueWithPrefix(), starGraph.getStar().get(0).getS_type(), question, question_tagged, query, starGraph.toString(), 2, GeneratedQuestion.QT_WHICH, GeneratedQuestion.SH_STAR_MODIFIED));
            question = T + " has at most 6 " + BasicNLP_FromPython.nounPlural(p.toLowerCase()) + "?";
            question_tagged = "<t>" + T + "</t> has <op>at most</op> <o>6</o> <p>" + BasicNLP_FromPython.nounPlural(p.toLowerCase()) + "</p>?";
          
            allPossibleQuestions.add(new GeneratedQuestion(starGraph.getStar().get(0).getSubject().getValueWithPrefix(), starGraph.getStar().get(0).getS_type(), question, question_tagged, query, starGraph.toString(), 2, GeneratedQuestion.QT_TOPICAL_PRUNE, GeneratedQuestion.SH_STAR_MODIFIED));
        }
    }

    public String selectQuery(StarGraph starGraph, int n, boolean least) {
        String query = "";
        String triples = "";
        ArrayList<TriplePattern> star = starGraph.getStar();
        String T = starGraph.getSeedType();
        triples += "\n\t?Seed \t rdf:type \t <" + T + "> .";

        triples += "\n\t" + star.get(0).toQueryTriplePattern().replace("<" + star.get(0).getSubject().getValueWithPrefix() + ">", "?Seed").replace(" " + star.get(0).getSubject().getValueWithPrefix() + " ", "?Seed")
                            .replace("\"" + star.get(0).getSubject().getValueWithPrefix() + "\"", "?Seed").replace("\"" + star.get(0).getSubject().getValueWithPrefix() + "\"^^xsd:dateTime ", "?Seed").
                replace("<" + star.get(0).getObject().getValueWithPrefix() + ">", "?count").replace(" " + star.get(0).getObject().getValueWithPrefix() + " ", "?count")
                            .replace("\"" + star.get(0).getObject().getValueWithPrefix() + "\"", "?count").replace("\"" + star.get(0).getObject().getValueWithPrefix() + "\"^^xsd:dateTime ", "?count") + ". ";

        if (least) {
            query = "SELECT DISTINCT ?Seed WHERE{ "
                    + triples
                    + "\n}"
                    + " GROUP BY ?Seed \n"
                    + " HAVING (COUNT(?count) >= " + n + ")";
        } else {
            query = "SELECT DISTINCT ?Seed WHERE{ "
                    + triples
                    + "\n}"
                    + " GROUP BY ?Seed \n"
                    + " HAVING (COUNT(?count) <= " + n + ")";
        }

        return query;
    }

    public ArrayList<GeneratedQuestion> getAllPossibleQuestions() {
        return allPossibleQuestions;
    }

    public void setAllPossibleQuestions(ArrayList<GeneratedQuestion> allPossibleQuestions) {
        this.allPossibleQuestions = allPossibleQuestions;
    }

    @Override
    public void generateAllPossibleQuestions() throws Exception {

    }
}
