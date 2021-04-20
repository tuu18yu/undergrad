#####################################################################
#
# CSC258H5S Fall 2020 Assembly Final Project
# University of Toronto, St. George
#
# Student: Yu Jin Kim, 1004926681
#
# Bitmap Display Configuration:
# - Unit width in pixels: 8
# - Unit height in pixels: 8
# - Display width in pixels: 256
# - Display height in pixels: 256
# - Base Address for Display: 0x10008000 ($gp)
#
# Which milestone is reached in this submission?
# (See the assignment handout for descriptions of the milestones)
# - Milestone 1/2/3/4/5
# 
#
# Which approved additional features have been implemented?
# (See the assignment handout for the list of additional features)
# 1. Game Over/Retry 
# 2. Score count, did not implement display :(
# 3. Added sound effects
# 4. Added black fragile platform
# Not here yet
#
# Any additional information that the TA needs to know:
# - (write here, if any)
#
#####################################################################

.data

	#Screen
	displayAddressBeginning: .word 0x10008000
	displayAddressEnd: .word 0x10008ffc
	screenWidth: .word 32
	
	#Colours
	backgroundColour: .word 0xC1F0FC #light blue
	platformColour: .word 0x55DE83 #light green
	doodlerColour: .word 0x35A431 #dark green
	blackColour: .word 0x000000
	
	#platform position information
	platform1: .word 0
	platform2: .word 0
	platform3: .word 0
	platform4: .word 0
	platform5: .word 0
	platform6: .word 0

	
	platformPositionX: .word 0
	platformPositionY: .word 0
	
	
	#doodler information
	doodlerPositionX: .word 0
	doodlerPositionY: .word 0
	
	#score variable
	score: 		.word 0
	#stores point for each time doodler hits platform
	scoreGain:	.word 10

	#end game message
	gameOverMessage: .asciiz "Game Over:( Your score was: "
	replayMessage:	.asciiz "Would you like to replay?"
	


.text

lw $gp, displayAddressBeginning

FillInBackground:
	lw $t0, displayAddressBeginning 
	lw $t1, displayAddressEnd
	lw $a0, backgroundColour 
	
	whileBackground:
		beq $t0, $t1, exitBackground #loops until $t0 == $t2
		j fillIn #jumps to fillIn
	
	exitBackground:
		sw $a0, ($t0)
		j InitialDisplay
	
	fillIn:
		sw $a0, ($t0)	#fills in the display
		addi $t0, $t0, 4 # $t0 increments by 4
		j whileBackground #loops back to while

InitialDisplay:

	li $s1, 1
	jal GeneratePlatform
	sw $t0, platform6
	lw $a0, platformColour
	jal DrawPlatform
	
	li $s1, 7
	jal GeneratePlatform
	sw $t0, platform1
	lw $a0, platformColour
	jal DrawPlatform
	
	li $s1, 13
	jal GeneratePlatform
	sw $t0, platform2
	lw $a0, blackColour
	jal DrawPlatform
	
	li $s1, 19
	jal GeneratePlatform
	sw $t0, platform3
	lw $a0, platformColour
	jal DrawPlatform
	
	li $s1, 25
	jal GeneratePlatform
	sw $t0, platform4
	lw $a0, platformColour
	jal DrawPlatform
	
	li $s1, 31
	jal GeneratePlatform
	sw $t0, platform5
	lw $a0, platformColour
	jal DrawPlatform
	
	lw $a3, doodlerColour
	lw $t0, platformPositionX
	addi $t0, $t0, 4
	li $t1, 30
	sw $t0, doodlerPositionX
	sw $t1, doodlerPositionY
	jal DrawDoodler
	
	#Sleep
	li $v0, 32
	li $a0, 1000
	syscall
		
	j main
	

	


	
	
		
	
main:
	
	
	
	#Clear userInput
	
	
	#Check for keyboard input
	
	checkInput:
		#Sleep
		li $v0, 32
		li $a0, 50
		syscall
		
		lw $t8, 0xffff0004
		beq $t8, 0x6b, MoveDoodlerLeft
		beq $t8, 0x6a, MoveDoodlerRight
		j checkDownPlatform
		
	MoveDoodlerLeft:

		lw $a0, doodlerPositionX
		li $s4, 31
	
		#delete doodler in current position
		jal DeleteDoodler
		lw $a0, doodlerPositionX #load current position back
		beq $a0, $s4, backInRangeL #check new position is in 0-31
		add $a0, $a0, 1 # move Doodler left
	
		drawLeft:
			sw $a0, doodlerPositionX # save the new platform position
	
			#draw platform at new position
			lw $a3, doodlerColour
			lw $a0, doodlerPositionX 
	
			j checkDownPlatform
	
	MoveDoodlerRight:
		lw $a0, doodlerPositionX
		
		li $s4, 0
	
		#delete doodler in current position
		jal DeleteDoodler
		lw $a0, doodlerPositionX #load current position back
		beq $a0, $s4, backInRangeR #check if current position is on the edge
		subi $a0, $a0, 1 # move Doodler right

		drawRight:
			sw $a0, doodlerPositionX # save the new platform position
		
			#draw platform at new position
			lw $a3, doodlerColour
			lw $a0, doodlerPositionX 
	
			j checkDownPlatform

		
		
	CheckUpdatePlatform:
		lw $s0, doodlerPositionY
		li $s1, 20
		ble $s0, $s1, UpdatePlatform
		j MoveDoodlerDown
		
	#update platform location
	UpdatePlatform:
		#delete platforms
		lw $a0, backgroundColour
	
		lw $t0, platform6
		jal DrawPlatform
		
		lw $t0, platform5
		jal DrawPlatform
	
		lw $t0, platform4
		jal DrawPlatform
	
		lw $t0, platform3
		jal DrawPlatform
	
		lw $t0, platform2
		jal DrawPlatform
	
		lw $t0, platform1
		jal DrawPlatform
		
		#move address of platforms
		jal PlatformDown
	
		lw $s0, displayAddressEnd
		bgt $t1, $s0, GeneratePlatformTop1
		
		lw $s0, displayAddressEnd
		bgt $t2, $s0, GeneratePlatformTop2
		
		lw $s0, displayAddressEnd
		bgt $t3, $s0, GeneratePlatformTop3
		
		lw $s0, displayAddressEnd
		bgt $t4, $s0, GeneratePlatformTop4
		
		lw $s0, displayAddressEnd
		bgt $t5, $s0, GeneratePlatformTop5
		
		lw $s0, displayAddressEnd
		bgt $t6, $s0, GeneratePlatformTop6
	
	GeneratePlatformTop1:
		li $s1, 1
		jal GeneratePlatform
		sw $t0, platform1
		lw $a0, platformColour
		j DrawPlatforms
		
	GeneratePlatformTop2:
		li $s1, 1
		jal GeneratePlatform
		sw $t0, platform2
		lw $a0, platformColour
		j DrawPlatforms
		
	GeneratePlatformTop3:
		li $s1, 1
		jal GeneratePlatform
		sw $t0, platform3
		lw $a0, platformColour
		j DrawPlatforms
		
	GeneratePlatformTop4:
		li $s1, 1
		jal GeneratePlatform
		sw $t0, platform4
		lw $a0, platformColour
		j DrawPlatforms
		
	GeneratePlatformTop5:
		li $s1, 1
		jal GeneratePlatform
		sw $t0, platform5
		lw $a0, platformColour
		j DrawPlatforms
		
	GeneratePlatformTop6:
		li $s1, 1
		jal GeneratePlatform
		sw $t0, platform6
		lw $a0, platformColour
		j DrawPlatforms
		
	DrawPlatforms:
		lw $a0, platformColour
	
		lw $t0, platform6
		jal DrawPlatform
		
		lw $t0, platform5
		jal DrawPlatform
		
		
		lw $t0, platform4
		jal DrawPlatform
	
		lw $t0, platform3
		jal DrawPlatform
		
		lw $a0, blackColour
		lw $t0, platform2
		jal DrawPlatform
		
		lw $a0, platformColour
		lw $t0, platform1
		jal DrawPlatform
		
		j MoveDoodlerDown
		


	#Sleep
		# li $v0, 32
		# li $a0, 1000
		# syscall
		
		# jal PlatformDown
	#go back

	 		
	 
	

GeneratePlatform:

	#RandomX
	li $v0, 42
	li $a0, 0
	li $a1, 24
	syscall
	
	sw $a0, platformPositionX
	lw $s0, platformPositionX

	lw $t0, screenWidth	#Store screen width into $v0
	mul $t0, $t0, $s1	#multiply by y position
	add $t0, $t0, $s0	#add the x position
	mul $t0, $t0, 4		#multiply by 4
	add $t0, $t0, $gp	#add global pointerfrom bitmap display
	
	jr $ra

	
	
	
DrawPlatform:
	li $t1, 0
	addi $t1, $t0, 32
	
	whilePlatform:
		beq $t0, $t1, exitPlatform
		j drawPlatform
	
	exitPlatform:
		sw $a0, ($t0)
		jr $ra
	
	drawPlatform:
		sw $a0, ($t0)
		addi $t0, $t0, 4
		j whilePlatform
		


		
DrawDoodler:
	lw $a1, doodlerPositionY
	lw $a0, doodlerPositionX
	lw $v0, screenWidth	#Store screen width into $v0
	mul $v0, $v0, $a1	#multiply by y position
	add $v0, $v0, $a0	#add the x position
	mul $v0, $v0, 4		#multiply by 4
	add $v0, $v0, $gp	#add global pointerfrom bitmap display
	
	li $s5, 29
	
	bgt $a0, $s5, drawDoodlerBottomEdge
	
	drawDoodlerBottom:
		sw $a3, 0($v0)
		add $v1, $v0, 8
		sw $a3, 0($v1)
		j drawDoodlerRest
		
	drawDoodlerBottomEdge:
		sw $a3, 0($v0)
		sub $v1, $v0, 120
		sw $a3, 0($v1)
		j drawDoodlerRest
		
	drawDoodlerRest:
		li $s5, 30
		li $s6, 31
		
		sub $v0, $v0, 128
		sw $a3, 0($v0)
		sub $v0, $v0, 128
		sw $a3, 0($v0)
		
		sub $v1, $v1, 128
		sw $a3, 0($v1)
		sub $v1, $v1, 128
		sw $a3, 0($v1)
		
		beq $a0, $s6, drawDoodlerCenter
		
		add $v0, $v0, 4
		sw $a3, 0($v0)
		add $v0, $v0, 128
		sw $a3, 0($v0)
		jr $ra

	
	drawDoodlerCenter:
		sub $v1, $v1, 4
		sw $a3, 0($v1)
		add $v1, $v1, 128
		sw $a3, 0($v1)
		jr $ra
		
		
saveBackground0:
	lw $t0, 0($v0)
	sw $t0, 0($v0)
	j checkDoodlerBottom1
	
saveBackground1:
	lw $t0, 0($v1)
	sw $t0, 0($v1)
	j checkDoodlerRest1
	
saveEdgeBackground0:
	lw $t0, 0($v0)
	sw $t0, 0($v0)
	j checkDoodlerBottomEdge1
	
saveEdgeBackground1:
	lw $t0, 0($v1)
	sw $t0, 0($v1)
	j checkDoodlerRest1
	
saveRestBackground1:
	lw $t0, 0($v0)
	sw $t0, 0($v0)
	j checkDoodlerRest2
	
saveRestBackground2:
	lw $t0, 0($v0)
	sw $t0, 0($v0)
	j checkDoodlerRest3
	
saveRestBackground3:
	lw $t0, 0($v1)
	sw $t0, 0($v1)
	j checkDoodlerRest4
	
saveRestBackground4:
	lw $t0, 0($v1)
	sw $t0, 0($v1)
	j checkDoodlerRest5
	
saveRestBackground5:
	lw $t0, 0($v0)
	sw $t0, 0($v0)
	j checkDoodlerRest6
	
saveRestBackground6:
	lw $t0, 0($v0)
	sw $t0, 0($v0)
	jr $ra
	
saveDoodlerCenter1:
	lw $t0, 0($v1)
	sw $t0, 0($v1)
	j checkDoodlerCenter1
	
saveDoodlerCenter2:
	lw $t0, 0($v1)
	sw $t0, 0($v1)
	jr $ra
	
	
DeleteDoodler:
	lw $a1, doodlerPositionY
	lw $a0, doodlerPositionX
	lw $v0, screenWidth	#Store screen width into $v0
	mul $v0, $v0, $a1	#multiply by y position
	add $v0, $v0, $a0	#add the x position
	mul $v0, $v0, 4		#multiply by 4
	add $v0, $v0, $gp	#add global pointerfrom bitmap display
	
	lw $a3, doodlerColour
	lw $t1, backgroundColour
	li $s5, 29
	
	bgt $a0, $s5, checkDoodlerBottomEdge0
	
	checkDoodlerBottom0:
		lw $t0, 0($v0)
		bne $a3, $t0, saveBackground0
		sw $t1, 0($v0)
		j checkDoodlerBottom1
	
	checkDoodlerBottom1:
		add $v1, $v0, 8
		lw $t0, 0($v1)
		bne $a3, $t0, saveBackground1
		sw $t1, 0($v1)
		j checkDoodlerRest1
		
	checkDoodlerBottomEdge0:
		lw $t0, 0($v0)
		bne $a3, $t0, saveEdgeBackground0
		sw $t1, 0($v0)
		j checkDoodlerBottomEdge1
		
	checkDoodlerBottomEdge1:
		sub $v1, $v0, 120
		lw $t0, 0($v1)
		bne $a3, $t0, saveEdgeBackground1
		sw $t1, 0($v0)
		j checkDoodlerRest1
		
	
	checkDoodlerRest1:
		li $s5, 30
		li $s6, 31
		
		sub $v0, $v0, 128
		lw $t0, 0($v0)
		bne $a3, $t0, saveRestBackground1
		sw $t1, 0($v0)
		j checkDoodlerRest2
		
	checkDoodlerRest2:
		sub $v0, $v0, 128
		lw $t0, 0($v0)
		bne $a3, $t0, saveRestBackground2
		sw $t1, 0($v0)
		j checkDoodlerRest3
	
	checkDoodlerRest3:	
		sub $v1, $v1, 128
		lw $t0, 0($v1)
		bne $a3, $t0, saveRestBackground3
		sw $t1, 0($v1)
		j checkDoodlerRest4
	
	checkDoodlerRest4:	
		sub $v1, $v1, 128
		lw $t0, 0($v1)
		bne $a3, $t0, saveRestBackground4
		sw $t1, 0($v1)
		
		beq $a0, $s6, checkDoodlerCenter1
		j checkDoodlerRest5
		
	checkDoodlerRest5:
		add $v0, $v0, 4
		lw $t0, 0($v0)
		bne $a3, $t0, saveRestBackground5
		sw $t1, 0($v0)
		j checkDoodlerRest6
	
	checkDoodlerRest6:
		add $v0, $v0, 128
		lw $t0, 0($v0)
		bne $a3, $t0, saveRestBackground6
		sw $t1, 0($v0)
		jr $ra

	
	checkDoodlerCenter1:
		sub $v1, $v1, 4
		lw $t0, 0($v1)
		bne $a3, $t0, saveDoodlerCenter1
		sw $t1, 0($v1)
		j checkDoodlerCenter2
		
	checkDoodlerCenter2:	
		add $v1, $v1, 128
		lw $10, 0($v1)
		bne $a3, $t0, saveDoodlerCenter2
		sw $t1, 0($v1)
		jr $ra
	


PlatformDown:
	lw $t0, platform6
	add $t0, $t0, 768
	sw $t0, platform6
	lw $t4, platform6
	add $t4, $t4, 32
	
	lw $t0, platform5 #load current position
	add $t0, $t0, 768 # move platform down
	sw $t0, platform5 # save the new platform position
	lw $t5, platform5
	add $t5, $t5, 32
	
	lw $t0, platform4
	add $t0, $t0, 768
	sw $t0, platform4
	lw $t4, platform4
	add $t4, $t4, 32

	lw $t0, platform3
	add $t0, $t0, 768
	sw $t0, platform3
	lw $t3, platform3
	add $t3, $t3, 32
	

	lw $t0, platform2 #load current position back
	add $t0, $t0, 768 # move platform down
	sw $t0, platform2 
	lw $t2, platform2
	add $t2, $t2, 32
	
	
	lw $t0, platform1
	add $t0, $t0, 768
	sw $t0, platform1
	lw $t1, platform1
	add $t1, $t1, 32

	jr $ra

	
IncreaseScore:
	lw $s7, score
	lw $s6, scoreGain
	add $s7, $s7, $s6
	sw $s7, score
	jr $ra
		

	
	
MoveDoodlerDown:
	lw $t8, 0xffff0004 #check Input
	beq $t8, 0x6b, MoveDoodlerLeft
	beq $t8, 0x6a, MoveDoodlerRight
	
	checkDownPlatform:
		lw $s1, doodlerPositionY
		li $s6, 31
		bge $s1, $s6, EndGame
		lw $s1, doodlerPositionY
		lw $s0, doodlerPositionX
		lw $v0, screenWidth	#Store screen width into $v0
		mul $v0, $v0, $s1	#multiply by y position
		add $v0, $v0, $s0	#add the x position
		mul $v0, $v0, 4		#multiply by 4
		add $v0, $v0, $gp	#add global pointerfrom bitmap display

		
		checkDownPlatform1:
			lw $s3, platform1
			subi $s3, $s3, 128
			bge $v0, $s3, checkDownSmallerPlatform1
			j checkDownPlatform2
		
		checkDownPlatform2:
			lw $s3, platform2
			subi $s3, $s3, 128
			bge $v0, $s3, checkDownSmallerPlatform2
			j checkDownPlatform3
			
		checkDownPlatform3:
			lw $s3, platform3
			subi $s3, $s3, 128
			bge $v0, $s3, checkDownSmallerPlatform3
			j checkDownPlatform4
			
		checkDownPlatform4:
			lw $s3, platform4
			subi $s3, $s3, 128
			bge $v0, $s3, checkDownSmallerPlatform4
			j checkDownPlatform5
			
		checkDownPlatform5:
			lw $s3, platform5
			subi $s3, $s3, 128
			bge $v0, $s3, checkDownSmallerPlatform5
			j checkDownPlatform6
		
		checkDownPlatform6:
			lw $s3, platform6
			subi $s3, $s3, 128
			bge $v0, $s3, checkDownSmallerPlatform6
			j moveDown		
	
		checkDownSmallerPlatform1:
			add $s3, $s3, 32
			ble $v0, $s3, MoveDoodlerUp
			j checkDownPlatform2
			
		checkDownSmallerPlatform2:
			add $s3, $s3, 32
			ble $v0, $s3, FragilePlatform
			j checkDownPlatform3
			
		FragilePlatform:
	
			lw $a0, backgroundColour
			lw $t0, platform2
			jal DrawPlatform
			
			li $s1, 37
			li $s2, 6

			lw $t0, screenWidth	#Store screen width into $v0
			mul $t0, $t0, $s1	#multiply by y position
			add $t0, $t0, $s0	#add the x position
			mul $t0, $t0, 4		#multiply by 4
			add $t0, $t0, $gp	#add global pointerfrom bitmap display
			
			sw $t0, platform2
			j MoveDoodlerUp
			
		checkDownSmallerPlatform3:
			add $s3, $s3, 32
			ble $v0, $s3, MoveDoodlerUp
			j checkDownPlatform4
			
		checkDownSmallerPlatform4:
			add $s3, $s3, 32
			ble $v0, $s3, MoveDoodlerUp
			j checkDownPlatform5
			
		checkDownSmallerPlatform5:
			add $s3, $s3, 32
			ble $v0, $s3, MoveDoodlerUp
			j checkDownPlatform6
			
		checkDownSmallerPlatform6:
			add $s3, $s3, 32
			ble $v0, $s3, MoveDoodlerUp
			j moveDown
		
	moveDown:
	
		#delete doodler in current position
		jal DeleteDoodler
		lw $s1, doodlerPositionY #load current position back
		add $s1, $s1, 3 # move doodler down
		sw $s1, doodlerPositionY # save the new platform position
	
		#draw doodler at new position
		lw $a3, doodlerColour
		lw $s1, doodlerPositionY 
		jal DrawDoodler
		
		li $v0, 32
		li $a0, 1000
		syscall
		
		j DrawPlatforms
	
	
MoveDoodlerUp:
	li $v0, 31
	li $a0, 80
	li $a1, 250
	li $a2, 32
	li $a3, 127
	syscall
		
	li $s4, 2
	lw $a1, doodlerPositionY
	
	jal IncreaseScore
		
	moveUp:
		#delete doodler in current position
		jal DeleteDoodler
		lw $a1, doodlerPositionY #load current position back
		sub $a1, $a1, 9 # move doodler up
		sw $a1, doodlerPositionY # save the new platform position
	
		#draw platform at new position
		lw $a3, doodlerColour
		lw $a1, doodlerPositionY 
		jal DrawDoodler
		
		li $v0, 32
		li $a0, 1000
		syscall
		
		j CheckUpdatePlatform
	
	
	
	
CoordinateToAddress:
	lw $v0, screenWidth 	#Store screen width into $v0
	mul $v0, $v0, $a1	#multiply by y position
	add $v0, $v0, $a0	#add the x position
	mul $v0, $v0, 4		#multiply by 4
	add $v0, $v0, $gp	#add global pointerfrom bitmap display
	jr $ra			# return $v0
	
backInRangeL:
	li $a0, 0
	j drawLeft
	
backInRangeR:
	li $a0, 31
	j drawRight
	
					




	
	

	


	

EndGame:
	FillInBackgroundEnd:
		lw $t0, displayAddressBeginning 
		lw $t1, displayAddressEnd
		lw $a0, blackColour
	
	whileBackgroundEnd:
		beq $t0, $t1, exitBackgroundEnd #loops until $t0 == $t2
		j fillInEnd #jumps to fillIn
	
	exitBackgroundEnd:
		
		#play a sound tune 
		li $v0, 31
		li $a0, 28
		li $a1, 250
		li $a2, 32
		li $a3, 127
		syscall
		
		li $a0, 33
		li $a1, 250
		li $a2, 32
		li $a3, 127
		syscall
	
		li $a0, 47
		li $a1, 1000
		li $a2, 32
		li $a3, 127
		syscall
		
		sw $a0, ($t0)
		
		li $v0, 56 
		la $a0, gameOverMessage #get message
		lw $a1, score	#get score
		syscall
	
		li $v0, 50 
		la $a0, replayMessage #get message
		syscall
	
		beqz $a0, FillInBackground #jump back to start of program
		#end program
		li $v0, 10
		syscall
		
		
	
	fillInEnd:
		sw $a0, ($t0)	#fills in the display
		addi $t0, $t0, 4 # $t0 increments by 4
		#Sleep
		li $v0, 32
		li $a0, 5
		syscall
		j whileBackgroundEnd #loops back to while
		
		
Exit:
	li $v0, 10 # terminate the program gracefully
	syscall
