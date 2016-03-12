package EntityWindow;
//共指完之后按句子切分，然后在计算距离 按特殊字符切分时需要+\\
public class RelateScope {
    public boolean compute(String en1,String en2,String text){
    	//System.out.println(text.indexOf("."));
    	boolean flag=false;
    	String[]str=text.split("\\.");
    	System.out.println("切句后有"+str.length+"个句子.");
    	for(int k=0;k<str.length;k++){
    		int i=str[k].indexOf(en1);
    	//	System.out.println(i);
        	int j=str[k].indexOf(en2);
        //	System.out.println(j);
        //	System.out.println((j-i)/6);
        	if(i!=-1&&j!=-1&&(i-j)/6<25&&(i-j)/6>(-25)){
        		flag=true;
        		break;
        	}//end if
    	}
    	return flag;
    } //end func
    
   public static void main(String[] args){
	   String en1="Werner Zwingmann";
	   String en2="European Union";
	   String text="The European Commission said on Thursday it disagreed with German advice to consumers to shun British lamb until scientists determine whether mad cow disease can be transmitted to sheep .  Germany 's representative to the European Union 's veterinary committee Werner Zwingmann said on Wednesday consumers should buy sheepmeat from countries other than Britain until the scientific advice was clearer . We don't support any such recommendation because we do n't see any grounds for it , the Commission 's chief spokesman Nikolaus van der Pas told a news briefing .  He said further scientific study was required and if it was found that action was needed it should be taken by the European Union .  He said a proposal last month by EU Farm Commissioner Franz Fischler to ban sheep brains , spleens and spinal cords from the human and animal food chains was a highly specific and precautionary move to protect human health .  Fischler proposed EU-wide measures after reports from Britain and France that under laboratory conditions sheep could contract Bovine Spongiform Encephalopathy ( BSE ) -- mad cow disease .  But Fischler agreed to review his proposal after the EU 's standing veterinary committee , mational animal health officials , questioned if such action was justified as there was only a slight risk to human health .  Spanish Farm Minister Loyola de Palacio had earlier accused Fischler at an EU farm ministers ' meeting of causing unjustified alarm through dangerous generalisation .   .  Only France and Britain backed Fischler 's proposal .  The EU 's scientific veterinary and multidisciplinary committees are due to re-examine the issue early next month and make recommendations to the senior veterinary officials .  Sheep have long been known to contract scrapie , a brain-wasting disease similar to BSE which is believed to have been transferred to cattle through feed containing animal waste .  British farmers denied on Thursday there was any danger to human health from their sheep , but expressed concern that German government advice to consumers to avoid British lamb might influence consumers across Europe .   What we have to be extremely careful of is how other countries are going to take Germany 's lead ,  Welsh National Farmers ' Union ( NFU ) chairman John Lloyd Jones said on BBC radio .  Bonn has led efforts to protect public health after consumer confidence collapsed in March after a British report suggested humans could contract an illness similar to mad cow disease by eating contaminated beef .  Germany imported 47,600 sheep from Britain last year , nearly half of total imports .  It brought in 4,275 tonnes of British mutton , some 10 percent of overall imports.";
	   RelateScope rs=new RelateScope();
	  System.out.println(rs.compute(en1, en2, text));
   }
}
