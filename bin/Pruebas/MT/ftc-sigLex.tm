#states
q0
q1
q2
qa
#initial
q0
#accepting
qa
#inputAlphabet
a
b
c
#tapeAlphabet
a
b
c
#transitions
q0:a?q0:a:>
q0:b?q0:b:>
q0:c?q0:c:>
q0:!?q1:!:<
q1:c?q1:a:<
q1:!?qa:a:-
q1:a?q2:b:<
q1:b?q2:c:<
q2:a?q2:a:<
q2:b?q2:b:<
q2:c?q2:c:<
q2:!?qa:!:>