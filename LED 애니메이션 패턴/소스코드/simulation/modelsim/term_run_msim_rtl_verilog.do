transcript on
if {[file exists rtl_work]} {
	vdel -lib rtl_work -all
}
vlib rtl_work
vmap work rtl_work

vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/term.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/DEC0.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/DEC1.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/DEC2.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/DEC3.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/DEC4.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/DEC5.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/DEC6.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/DEC7.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/DEC8.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/DEC9.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/DEC10.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/DEC11.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/DEC12.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/DEC13.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/DEC14.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/DEC15.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/DEC16.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/DEC17.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/LED_DEC0.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/LED_DEC1.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/LED_DEC2.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/modulo_k_counter.v}
vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/remote.v}

vlog -vlog01compat -work work +incdir+C:/Users/Jonghyeon/Desktop/quartus\ projects/term {C:/Users/Jonghyeon/Desktop/quartus projects/term/test_bench.v}

vsim -t 1ps -L altera_ver -L lpm_ver -L sgate_ver -L altera_mf_ver -L altera_lnsim_ver -L cyclonev_ver -L cyclonev_hssi_ver -L cyclonev_pcie_hip_ver -L rtl_work -L work -voptargs="+acc"  test_bench

add wave *
view structure
view signals
run -all
