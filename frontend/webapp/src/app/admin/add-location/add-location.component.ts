import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs';
import { LocationsService } from 'src/app/services/locations.service';

@Component({
  selector: 'app-add-location',
  templateUrl: './add-location.component.html',
  styleUrls: ['./add-location.component.css']
})
export class AddLocationComponent implements OnInit {
  form!: FormGroup;
  loading = false;
  submitted = false;
  types: any[] = [
    {label: 'Location', value: 'Location'},
    {label: 'Depot', value: 'Depot'}
  ]
  
  constructor(
    private formBuilder: FormBuilder,
    private locationService: LocationsService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.form = this.formBuilder.group({
      nameControl: new FormControl('', Validators.required),
      selectedType: new FormControl('Location', Validators.required)
    })
  }

  onSubmit() {
    this.submitted = true;

    if(this.form.invalid) {
      return;
    }

    this.loading = true;

    this.locationService.addLocation(
      this.form.get('nameControl')?.value,
      this.form.get('selectedType')?.value
    ).pipe(first())
    .subscribe({
      next: () => {
        if(localStorage['addLocation'] == '\"OK\"') {
          this.goToLocations();
        }
      },
      error: error => {
        console.log(error);
        this.loading = false;
      }
    })
  }

  goToLocations() {
    this.router.navigate(['../location'], {relativeTo: this.route});
  }
} 
