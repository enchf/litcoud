class UniqueEmailAddresses {
    public int numUniqueEmails(String[] emails) {
        Set<String> set = new HashSet<>();
        for (String email : emails) {
            String[] aux = email.split("@");
            // if (aux[0].indexOf("+") < 0 && aux[0].indexOf("\\.") < 0) {
            //     set.add(email);
            //     continue;
            // }
            aux[0] = aux[0].replaceAll("\\.", "");
            int index = aux[0].indexOf("+");
            if (index >= 0)
                aux[0] = aux[0].substring(0, index);
            StringBuilder sb = new StringBuilder(aux[0]).append("@").append(aux[1]);
            
            set.add(sb.toString()); 
        }
        return set.size();
        
    }
}
