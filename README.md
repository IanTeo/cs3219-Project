# CS3219 Project

## Duplicate Papers 
* Embedding NomLex-BR nominalizations into OpenWordnet-PT (W14-0152, W14-0156)
* OpenWordNet-PT: A Project Report (W14-0153, W14-0157)
* Issues in building English-Chinese parallel corpora with WordNets Francis Bond and Shan Wang (W14-0154, W14-0158)
* Power of Confidence: How Poll Scores Impact Topic Dynamics in Political Debates [Very different] (W14-2514, W14-2710)
* Extraction system for Personal Attributes Extraction of CLP2014 (W14-6829, W14-6830)


## Changes to XML files:

**D13-1138**
Changed line 550, remove "\<surname>" tag

**D14-1184**
Changed line 561, remove "\<surname>" tag

**W14-1115**
Changed line 561, remove "<", ">" tag

**W14-2609**
Changed line 926, remove "\<firstName>.\<lastName>" tag

**W14-3317**
Changed line 634, remove "\<surname>" tag

## Paper Counts

D12: 135<br>
D13: 201<br>
D14: 220<br>
D15: 308<br>
J14: 35<br>
Q14: 39<br>
W14: 1023<br>
Total: 1961

## Questions

### Global Statistics

CM: Counting Method (Count while parsing)<br>
WIC: Without Invalid Citations<br>
TLC: To Lower Case

**1. How many document are there in all datasets put together?**<br>
*COMMAND: count dataset*<br>
1961

**2. How many citations are there in all datasets put together?**<br>
*COMMAND: count citation*<br>
46824 (CM, WIC)<br>
48683 (CM)<br>
46662 (WIC, Missing duplicate dataset citations [5 datasets])<br>
48518 (Missing duplicate dataset citations [5 datasets])<br>
46654 (TLC)

**3. How many unique citations are there in all datasets put together?**<br>
*COMMAND: count unique citation*<br>
30372 (CM, WIC)<br>
31989 (CM)<br>
26941 (TLC)

**4. How many author are mentioned in the citations in all datasets put together?**<br>
*COMMAND: count citation author*
33986 (CM, WIC)<br>
32546 (WIC)<br>
33991 (WIC)<br>
33991 (TLC)

**5. What is the range of the years of the cited documents in all datasets put together?**<br>
*COMMAND: count citation range*<br>
137 (omitted year 1305)

### Transition over time
**6. For the conference D12 give number of cited documents published in each of the years 2000 to 2015.**<br>
*COMMAND: countyear 2000-2015*<br>

| WIC      | TLC      |
| ---------|:---------|
| 2000 91  | 2000 88  |
| 2001 77  | 2001 75  |
| 2002 130 | 2002 112 |
| 2003 145 | 2003 132 |
| 2004 184 | 2004 172 |
| 2005 198 | 2005 183 |
| 2006 249 | 2006 237 |
| 2007 297 | 2007 270 |
| 2008 300 | 2008 284 |
| 2009 333 | 2009 308 |
| 2010 391 | 2010 367 |
| 2011 437 | 2011 406 |
| 2012 82  | 2012 82  |
| 2013 0   | 2013 0   |
| 2014 0   | 2014 0   |
| 2015 0   | 2015 0   |

**7. Repeat the above step for conferences ‘EMNLP’ and ‘CoNLL’ (instead of years) for the con- ference D13.**<br>
*COMMAND: countconference emnlp, empirical methods in natural language processing*<br>
*COMMAND: countconference conll, computational natural language learning*<br>
EMNLP 362<br>
CoNLL 148

**8. For an author ‘Yoshua Bengio’ (also check for Y. Bengio) the number of times he is cited for his work authored in each of the years 2000 to 2015.**<br>
*COMMAND: countauthor 2000-2015 yoshua bengio, y bengio*<br>

| TLC      |
| ---------|
| 2000 0   |
| 2001 2   |
| 2002 1   |
| 2003 4   |
| 2004 0   |
| 2005 3   |
| 2006 4   |
| 2007 2   |
| 2008 3   |
| 2009 9   |
| 2010 10  |
| 2011 5   |
| 2012 12  |
| 2013 9   |
| 2014 17  |
| 2015 6   |

**9. For the conference J14,W14 find number of cited documents published in each of the years from 2010 to 2015.**<br>
*COMMAND: countyear 2010-2015*<br>

| Year | J14  | W14  |
| -----|:-----|:-----|
| 2010 | 144  | 1137 |
| 2011 | 111  | 1133 |
| 2012 | 104  | 1407 |
| 2013 | 40   | 1635 |
| 2014 | 7    | 821  |
| 2015 | 0    | 1    |

**10. Repeat the above step for conference ‘NAACL’ for conference Q14,D14**<br>
*COMMAND: countconference naacl, north american chapter of the association for computational linguistics*<br>
D14 203<br>
Q14 65
