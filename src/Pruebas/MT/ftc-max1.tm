#states
q0
q1
q2
q3
q4
q5
q6
q7
q8
q9
q10
q11
q12
#initial
q0
#accepting
q9
q12
#inputAlphabet
1
#tapeAlphabet
1
X
#transitions
q0:1?q1:X:>
q0:!?q5:!:<
q1:1?q1:1:>
q1:!?q2:!:>
q2:X?q2:X:>
q2:1?q3:X:<
q2:!?q10:!:<
q3:X?q3:X:<
q3:!?q4:!:<
q4:1?q4:1:<
q4:X?q0:X:>
q5:X?q5:!:<
q5:!?q6:!:>
q6:!?q6:!:>
q6:X?q7:1:>
q6:1?q8:1:-
q7:X?q7:1:>
q7:1?q8:1:<
q7:!?q8:!:<
q8:1?q8:1:<
q8:!?q9:!:>
q10:X?q10:!:<
q10:!?q11:!:<
q11:X?q11:1:<
q11:1?q11:1:<
q11:!?q12:!:>