head -n 1 $1 | cut -d" " -f1-2 > tmp && sed -n '1!p' $1 >> tmp
sed 's/_b_/_B_/g' tmp > tmp2 && mv tmp2 tmp
sed 's/_e_/_E_/g' tmp > tmp2 && mv tmp2 tmp
sed 's/_p_/_P_/g' tmp > tmp2 && mv tmp2 tmp
sed 's/_u_/_U_/g' tmp > tmp2 && mv tmp2 tmp
mv tmp $1